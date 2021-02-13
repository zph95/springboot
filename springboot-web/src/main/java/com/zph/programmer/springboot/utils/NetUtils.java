package com.zph.programmer.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * IP and Port Helper for RPC
 */
@Slf4j
public class NetUtils {

    public static final String LOCALHOST = "127.0.0.1";
    public static final String ANYHOST = "0.0.0.0";
    private static final int RND_PORT_START = 30000;
    private static final int RND_PORT_RANGE = 10000;
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}\\:\\d{1,5}$");
    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    private static volatile InetAddress LOCAL_ADDRESS = null;
    private static String localIP = null;

    public static int getRandomPort() {
        return RND_PORT_START + RANDOM.nextInt(RND_PORT_RANGE);
    }

    public static int getAvailablePort() {
        try (ServerSocket ss = new ServerSocket()) {
            ss.bind(null);
            return ss.getLocalPort();
        } catch (IOException e) {
            return getRandomPort();
        }
    }

    public static int getAvailablePort(int port) {
        if (port <= 0) {
            return getAvailablePort();
        }
        for (int i = port; i < MAX_PORT; i++) {
            try (ServerSocket ss = new ServerSocket(i)) {
                return i;
            } catch (IOException e) {
                // continue
            }
        }
        return port;
    }

    public static boolean isInvalidPort(int port) {
        return port <= MIN_PORT || port > MAX_PORT;
    }

    public static boolean isValidAddress(String address) {
        return ADDRESS_PATTERN.matcher(address).matches();
    }

    public static boolean isLocalHost(String host) {
        return host != null
                && (LOCAL_IP_PATTERN.matcher(host).matches()
                || host.equalsIgnoreCase("localhost"));
    }

    public static boolean isAnyHost(String host) {
        return "0.0.0.0".equals(host);
    }

    public static boolean isInvalidLocalHost(String host) {
        return host == null
                || host.length() == 0
                || host.equalsIgnoreCase("localhost")
                || host.equals("0.0.0.0")
                || (LOCAL_IP_PATTERN.matcher(host).matches());
    }

    public static boolean isValidLocalHost(String host) {
        return !isInvalidLocalHost(host);
    }

    public static InetSocketAddress getLocalSocketAddress(String host, int port) {
        return isInvalidLocalHost(host) ?
                new InetSocketAddress(port) : new InetSocketAddress(host, port);
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String name = address.getHostAddress();
        return (name != null
                && !ANYHOST.equals(name)
                && !LOCALHOST.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }

    public static String getLocalHost() {
        InetAddress address = getLocalAddress();
        return address == null ? LOCALHOST : address.getHostAddress();
    }

    /**
     * Find first valid IP from local network card
     *
     * @return first valid local IP
     */
    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }
        InetAddress localAddress = getLocalAddress0();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Exception e) {
            log.warn("getLocalAddress0:", e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                try {
                    NetworkInterface network = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        try {
                            InetAddress address = addresses.nextElement();
                            if (isValidAddress(address)) {
                                return address;
                            }
                        } catch (Throwable e) {
                            log.warn("getLocalAddress0", e);
                        }
                    }
                } catch (Exception e) {
                    log.warn("getLocalAddress0:", e);
                }
            }
        } catch (Exception e) {
            log.warn("getLocalAddress0:", e);
        }
        return localAddress;
    }

    /**
     * @param hostName
     * @return ip address or hostName if UnknownHostException
     */
    public static String getIpByHost(String hostName) {
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException e) {
            return hostName;
        }
    }

    public static String toAddressString(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

    public static InetSocketAddress toAddress(String address) {
        int i = address.indexOf(':');
        String host;
        int port;
        if (i > -1) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
            port = 0;
        }
        return new InetSocketAddress(host, port);
    }

    public static void joinMulticastGroup(MulticastSocket multicastSocket, InetAddress multicastAddress) throws IOException {
        setInterface(multicastSocket, multicastAddress instanceof Inet6Address);
        multicastSocket.setLoopbackMode(false);
        multicastSocket.joinGroup(multicastAddress);
    }

    public static void setInterface(MulticastSocket multicastSocket, boolean preferIpv6) throws IOException {
        boolean interfaceSet = false;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface i = interfaces.nextElement();
            Enumeration<InetAddress> addresses = i.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (preferIpv6 && address instanceof Inet6Address) {
                    multicastSocket.setInterface(address);
                    interfaceSet = true;
                    break;
                } else if (!preferIpv6 && address instanceof Inet4Address) {
                    multicastSocket.setInterface(address);
                    interfaceSet = true;
                    break;
                }
            }
            if (interfaceSet) {
                break;
            }
        }
    }

    /**
     * 获取客户端IP
     *
     * @return 客户端IP
     */
    public static String getClientIP() {

        String result = "0.0.0.0";
        RequestAttributes attributes = null;
        try {
            attributes = RequestContextHolder.currentRequestAttributes();
        } catch (Exception e) {
            return result;
        }

        ServletRequestAttributes sra = (ServletRequestAttributes) attributes;
        HttpServletRequest request = sra.getRequest();
        return getClientIP(request);
    }

    /**
     * 获取请求syscode
     *
     * @return
     */
    public static String getHeaderSyscode() {
        String syscode = "";
        RequestAttributes attributes = null;
        try {
            attributes = RequestContextHolder.currentRequestAttributes();
        } catch (Exception e) {
            return syscode;
        }

        ServletRequestAttributes sra = (ServletRequestAttributes) attributes;
        HttpServletRequest request = sra.getRequest();
        syscode = request.getHeader("syscode");
        return syscode;
    }

    public static String getClientIP(HttpServletRequest request) {
        String result = request.getHeader("X-Real-IP");
        if (StringUtils.isEmpty(result) && "unknown".equalsIgnoreCase(result)) {
            result = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isEmpty(result) && "unknown".equalsIgnoreCase(result)) {
            String[] ary = result.split(",");
            if (ary.length > 0) {
                result = ary[ary.length - 1];
            }
        }

        if (StringUtils.isEmpty(result) && "unknown".equalsIgnoreCase(result)) {
            result = request.getHeader("Remote_Addr");
        }

        if (StringUtils.isEmpty(result) && "unknown".equalsIgnoreCase(result)) {
            result = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(result) && "unknown".equalsIgnoreCase(result)) {
            result = request.getHeader("HTTP_CLIENT_IP");
        }


        if (StringUtils.isEmpty(result) && "unknown".equalsIgnoreCase(result)) {
            result = request.getRemoteAddr();
        }

        if (result == null || result.isEmpty() || result.equals("0.0.0.1") || !isIPV4(result)) {
            result = "0.0.0.0";
        }
        return result;
    }

    /**
     * 判断IP是否是ipv4格式
     *
     * @param ip {@link String} ip地址
     * @return {@link Boolean} 返回IP地址是否是ipv4格式
     */
    private static Boolean isIPV4(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        return Pattern.matches("^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$", ip);
    }

    /**
     * 获取本机IP列表
     *
     * @return 获取本机IP列表
     */
    public static List<String> getLocalIPList() throws SocketException {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    // IPV4
                    if (inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            log.error("SocketException:", e);
            throw e;
        }
        return ipList;
    }

    /**
     * 获得服务端（本地）IP
     *
     * @return {@link String} 返回本机第一个IPV4地址
     */
    public static String getLocalIP() {
        if (localIP != null) {
            return localIP;
        }
        try {
            List<String> ipList = getLocalIPList();
            for (String ip : ipList) {
                if (ip != "127.0.0.1") {
                    localIP = ip;
                    break;
                }
            }
        } catch (SocketException e) {
            localIP = "0.0.0.0";
        }
        return localIP;
    }

}
