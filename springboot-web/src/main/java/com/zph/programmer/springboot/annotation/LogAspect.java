package com.zph.programmer.springboot.annotation;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Aspect
@Slf4j
@Component
public class LogAspect {


    /**
     * Controller层切点
     * 注意修改包路径
     */
    @Pointcut("@annotation(com.zph.programmer.springboot.annotation.ControllerLog)")
    public void controllerAspect() {

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



    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    private static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(ControllerLog.class).value();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doControllerBefore(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //请求方法
            String method = joinPoint.getSignature().getName() + "()";

            Object[] args = joinPoint.getArgs();
            Object[] arguments = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    //排除不能序列化的入参：MultipartFile
                    continue;
                }
                arguments[i] = args[i];
            }
            //方法参数
            String methodParam = Arrays.toString(arguments);
            Map<String, String[]> params = request.getParameterMap();
            StringBuilder decode = new StringBuilder();
            //针对get请求
            if (request.getQueryString() != null) {
                decode = new StringBuilder(URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8));
            } else {
                //针对post请求
                for (Map.Entry<String,String[]> entry : params.entrySet()) {
                    String key=entry.getKey();
                    String[] values = entry.getValue();
                    for (String value : values) {
                        decode.append(key).append("=").append(value).append("&");
                    }
                }
            }
            //将String根据&转成Map
            Map<String, Object> methodParamMap = transStringToMap(decode.toString(), "&", "=");

            //方法描述
            String methodDescription = getControllerMethodDescription(joinPoint);
            String sb = "\n" +
                    "********************************* " + className + "#" + method + "请求\n" +
                    "Description   :  " + methodDescription + "\n" +
                    "ContentType   :  " + (("".equals(request.getContentType()) || request.getContentType() == null) ? "FROM" : request.getContentType()) + "\n" +
                    "ServerAddress :  " + "(" + request.getMethod() + ")" + "\n" + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getRequestURI().substring(0, Math.min(request.getRequestURI().length(), 255)) +
                    "RequestParams :  " + (("".equals(decode.toString())) ? methodParam : methodParamMap) + "\n" +
                    "********************************* " + LocalDateTime.now().toString() + "\n";
            log.info(sb);
        } catch (Exception e) {
            log.warn("拦截Controller层记录用户的操作出错:",e);
        }
    }

    @AfterReturning(returning = "ret", pointcut = "controllerAspect()")
    public void doAfterControllerReturning(JoinPoint joinPoint, Object ret) {
        //类名
        String className = joinPoint.getTarget().getClass().getName();
        //请求方法
        String method = joinPoint.getSignature().getName() + "()";
        // 处理完请求，返回内容
        log.info("\n********************************* " + className + "#" + method + "方法执行结束");
    }


}
