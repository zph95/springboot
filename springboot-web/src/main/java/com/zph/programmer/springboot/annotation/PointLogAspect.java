package com.zph.programmer.springboot.annotation;

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
    @Pointcut("@annotation(com.zph.programmer.springboot.annotation.PointLog)")
    public void pointAspect() {

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
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(PointLog.class).value();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 前置通知 用于拦截记录日志
     *
     * @param joinPoint 切点
     */
    @Before("pointAspect()")
    public void doServiceBefore(JoinPoint joinPoint) {
        try {
            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //请求方法
            String method = joinPoint.getSignature().getName() + "()";
            //方法参数
            String methodParam = Arrays.toString(joinPoint.getArgs());

            //方法描述
            String methodDescription = getMethodDescription(joinPoint);
            String sb = "\n********************************* " + className + "#" + method + "服务\n" +
                    "Description   :  " + methodDescription + "\n" +
                    "RequestParams :  " + methodParam + "\n" +
                    "********************************* " + LocalDateTime.now().toString() + "\n";
            log.info(sb);
        } catch (Exception e) {
            log.error("拦截记录日志异常:",e);
        }
    }

    @AfterReturning(returning = "ret", pointcut = "pointAspect()")
    public void doAfterServiceReturning(JoinPoint joinPoint, Object ret) {
        //类名
        String className = joinPoint.getTarget().getClass().getName();
        //请求方法
        String method = joinPoint.getSignature().getName() + "()";

        String sb = "\n********************************* " + className + "#" + method + "服务执行结束\n"+
                "Return :"+ret.toString()+"\n"+
                "********************************* "+ LocalDateTime.now().toString() + "\n";;
        // 处理完请求，返回内容
        log.info(sb);
    }

    /**
     * 异常通知 用于拦截记录异常日志
     */
    @AfterThrowing(pointcut = "pointAspect()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        try {
            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //请求方法
            String method = (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            //方法描述
            String methodDescription = getMethodDescription(joinPoint);
            //获取用户请求方法的参数并序列化为JSON格式字符串
            StringBuilder params = new StringBuilder();
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    params.append(joinPoint.getArgs()[i]).append(";");
                }
            }
            String sb = "\n********************************* " + className + "#" + method + "方法执行异常\n" +
                    "Description      :  " + methodDescription + "\n" +
                    "Params           :  " + "[" + params.toString() + "]" + "\n" +
                    "ExceptionName    :  " + ex.getClass().getName() + "\n" +
                    "ExceptionMessage :  " + ex.getMessage() + "\n" +
                    "********************************* OVER\n";
            log.error(sb,ex);
        } catch (Exception e1) {
            log.error("拦截记录日志异常 : ",e1);
        }
    }
}
