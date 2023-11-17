package com.zph.programmer.springdemo.utils;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Sftp连接工具类
 */
@Slf4j
public class SftpUtils {

    private ChannelSftp sftp;

    private Session session;
    /**
     * FTP 登录用户名
     */
    private String userName;
    /**
     * FTP 登录密码
     */
    private String passWord;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * FTP 服务器地址IP地址
     */
    private String host;
    /**
     * FTP 端口
     */
    private int port;


    /**
     * 构造基于密码认证的sftp对象
     *
     * @param userName
     * @param passWord
     * @param host
     * @param port
     */
    public SftpUtils(String userName, String passWord, String host, int port) {
        this.userName = userName;
        this.passWord = passWord;
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     *
     * @param userName
     * @param host
     * @param port
     * @param privateKey
     */
    public SftpUtils(String userName, String host, int port, String privateKey) {
        this.userName = userName;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    public SftpUtils() {
    }

    public static void main(String[] args) throws Exception {
        SftpUtils sftp = new SftpUtils("admin", "password", "127.0.0.1", 22);
        sftp.login();
        String fileFullName = "output.xlsx";
        sftp.upload("working", "output.xlsx", fileFullName);
        sftp.logout();
    }

    /**
     * 连接sftp服务器
     */
    public void login() {
        try {
            JSch jsch = new JSch();
            if (null != privateKey) {
                jsch.addIdentity(privateKey);
                log.debug("Sftp connect, private key file：{}", privateKey);
            }
            log.debug("Sftp connect, host:{} username:{}", host, userName);

            session = jsch.getSession(userName, host, port);
            if (null != passWord) {
                session.setPassword(passWord);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            log.debug("Session connected");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            log.debug("Channel connected");

            sftp = (ChannelSftp) channel;
            log.debug(String.format("Sftp server host:[%s] port:[%s] connected", host, port));
        } catch (JSchException e) {
            log.error("Sftp connect failed, sftp server: {}:{} \n Exception message: {}", host, port, e.getMessage());
        }
    }

    /**
     * 关闭sftp连接
     */
    public void logout() {
        if (null != sftp) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.debug("Sftp closed");
            }
        }
        if (null != session) {
            if (session.isConnected()) {
                session.disconnect();
                log.debug("Session closed");
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName sftp端文件名
     * @param fileFullName 本地文件名
     */
    public void upload(String directory, String sftpFileName,
                       String fileFullName) throws Exception {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            log.warn("对方目录可能不存在[" + directory + "]，需要创建目录，异常信息为["
                    + e.getMessage() + "]");
            // 目录不存在，则创建文件夹
            String[] dirs = directory.split("/");
            String tempPath = "";
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) {
                    continue;
                }
                tempPath += "/" + dir;
                try {
                    log.info("检测目录[" + tempPath + "]");
                    sftp.cd(tempPath);
                } catch (SftpException ex) {
                    try {
                        log.info("创建目录[" + tempPath + "]");
                        sftp.mkdir(tempPath);
                        sftp.cd(tempPath);
                        log.info("进入目录[" + tempPath + "]");
                    } catch (Exception e1) {
                        log.error("创建目录[" + tempPath
                                + "]失败,异常信息[" + e1.getMessage() + "]");
                        throw new Exception("创建目录[" + tempPath
                                + "]失败,异常信息[" + e1.getMessage() + "]");
                    }
                } catch (Exception e1) {
                    log.error("创建目录[" + tempPath + "]失败,异常信息["
                            + e1.getMessage() + "]");
                    throw new Exception("创建目录[" + tempPath
                            + "]失败,异常信息[" + e1.getMessage() + "]");
                }
            }
            log.info("创建目录完成");
        } catch (Exception e1) {
            log.error("进入目录[" + directory + "]失败,异常信息["
                    + e1.getMessage() + "]");
            throw new Exception("进入目录[" + directory + "]失败,异常信息["
                    + e1.getMessage() + "]");
        }

        try {
            File file = new File(fileFullName);
            sftp.put(new FileInputStream(file), sftpFileName);
        } catch (Exception e) {
            log.error("Sftp文件上传异常，异常信息为[" + e.getMessage() + "]");
            throw new Exception("Sftp文件上传异常，异常信息为[" + e.getMessage() + "]");
        }
    }
}