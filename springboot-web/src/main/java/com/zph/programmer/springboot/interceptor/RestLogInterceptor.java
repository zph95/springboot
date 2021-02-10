package com.zph.programmer.springboot.interceptor;


import com.zph.programmer.springboot.servlet.BodyReaderHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class RestLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String body = null;
        if (request instanceof BodyReaderHttpServletRequestWrapper) {
            body = ((BodyReaderHttpServletRequestWrapper) request).getBody();
        }
        StringBuilder requestParamsBuilder = new StringBuilder();
        //针对get请求
        if (request.getQueryString() != null) {
            Map<String, Object> methodParamMap = transStringToMap(URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8), "&", "=");
            for(Map.Entry<String,Object> entry:methodParamMap.entrySet()) {
                requestParamsBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
            }

        } else {
            //针对post请求
            requestParamsBuilder.append(body);
        }

        String sb = "\n*********************************\n"  +
                "url    :   " + request.getRequestURI()+"\n"+
                "name   :   " + "name" + "\n" +
                "method :   " + request.getMethod()+"\n"+
                "ContentType   :  " + (("".equals(request.getContentType()) || request.getContentType() == null) ? "FROM" : request.getContentType()) + "\n" +
                "ServerAddress :  " + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +"\n"+
                "RequestParams :  " + (requestParamsBuilder.toString()) + "\n" +
                "********************************* " + LocalDateTime.now().toString() + "\n";
        log.info(sb);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * String 转Map
     *
     * @param mapString     待转的String
     * @param separator     分割符
     * @param pairSeparator 分离器
     */
    private static Map<String, Object> transStringToMap(String mapString, String separator, String pairSeparator) {
        Map<String, Object> map = new HashMap<>(16);
        String[] fSplit = mapString.split(separator);
        for (String s : fSplit) {
            if (s == null || s.length() == 0) {
                continue;
            }
            String[] sSplit = s.split(pairSeparator);
            String value = s.substring(s.indexOf('=') + 1);
            map.put(sSplit[0], value);
        }
        return map;
    }


}