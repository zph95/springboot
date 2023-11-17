package com.zph.programmer.springdemo.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class PointLogAspect {
    /**
     * 注意修改包路径
     */
    @Pointcut("@annotation(com.zph.programmer.springdemo.annotation.PointLog)")
    public void pointAspect() {

    }

    /**
     * 前置通知 用于拦截记录日志
     *
     * @param joinPoint 切点
     */
    @Before("pointAspect()")
    public void doLogBefore(JoinPoint joinPoint) {
        try {
            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //请求方法
            String method = joinPoint.getSignature().getName() + "()";
            //方法参数
            String methodParam = Arrays.toString(joinPoint.getArgs());

            //方法描述
            String methodDescription = getMethodDescription(joinPoint);
            String sb = "\n********************************* "+LocalDateTime.now().toString() + "\n" +
                    "ClassName     :  " + className +"\n"+
                    "Method        :  " + method +"\n" +
                    "Description   :  " + methodDescription + "\n" +
                    "RequestParams :  " + methodParam + "\n" +
                    "*********************************  Start\n";
            log.info(sb);
        } catch (Exception e) {
            log.error("拦截记录日志异常:",e);
        }
    }
    /**
     * 返回结果通知 用于拦截记录结果日志
     */
    @AfterReturning(returning = "ret", pointcut = "pointAspect()")
    public void doLogAfterReturning(JoinPoint joinPoint, Object ret) {
        try {
            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //请求方法
            String method = joinPoint.getSignature().getName() + "()";
            //方法描述
            String methodDescription = getMethodDescription(joinPoint);
            String sb = "\n********************************* " + LocalDateTime.now().toString() + "\n" +
                    "ClassName     :  " + className + "\n" +
                    "Method        :  " + method + "\n" +
                    "Description   :  " + methodDescription + "\n" +
                    "Return        :  " + ret.toString() + "\n" +
                    "********************************* End\n";
            log.info(sb);
        } catch (Exception e) {
            log.error("拦截记录日志异常:",e);
        }
    }

    /**
     * 异常通知 用于拦截记录异常日志
     */
    @AfterThrowing(pointcut = "pointAspect()", throwing = "ex")
    public void doLogAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        try {
            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //请求方法
            String method = joinPoint.getSignature().getName() + "()";
            //方法描述
            String methodDescription = getMethodDescription(joinPoint);
            //方法参数
            String methodParam = Arrays.toString(joinPoint.getArgs());
            String sb =  "\n********************************* "+LocalDateTime.now().toString() + "\n" +
                    "ClassName     :  " + className +"\n"+
                    "Method        :  " + method +"\n" +
                    "Description   :  " + methodDescription + "\n" +
                    "RequestParams :  " + methodParam + "\n" +
                    "ExceptionName    :  " + ex.getClass().getName() + "\n" +
                    "ExceptionMessage :  " + ex.getMessage() + "\n" +
                    "********************************* End\n";
            log.error(sb,ex);
        } catch (Exception e1) {
            log.error("拦截记录日志异常 : ",e1);
        }
    }

    /**
     * 获取注解中对方法的描述信息
     *
     * @param joinPoint 切点
     * @return 方法描述
     */
    private static String getMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] clazz = method.getParameterTypes();
                if (clazz.length == arguments.length) {
                    description = method.getAnnotation(PointLog.class).value();
                    break;
                }
            }
        }
        return description;
    }
}
