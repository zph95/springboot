package com.zph.programmer.springboot.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    private static Map<String,String> urlNameMap;

    /**
     * 获取应用上下文并获取相应的接口实现类
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        SpringBeanUtil.applicationContext = applicationContext;

        String[] beanNamesForAnnotation =  applicationContext.getBeanNamesForAnnotation(RestController.class);
        urlNameMap=new HashMap<>();
        //获取类对象
        for (String str : beanNamesForAnnotation) {
            Object bean = applicationContext.getBean(str);
            Class<?> forName = bean.getClass();
            //获取requestMapping注解的类
            RequestMapping declaredAnnotation = forName.getAnnotation(RequestMapping.class);
            if (declaredAnnotation != null) {
                String[] value = (declaredAnnotation.value());

                for (Method method : forName.getDeclaredMethods()) {
                    GetMapping annotation2 = method.getAnnotation(GetMapping.class);
                    if (annotation2 != null) {
                        //获取类的url路径
                        String urlPath = value[0] + annotation2.value()[0];
                        urlNameMap.put(urlPath , annotation2.name());
                    }
                }
            }
        }

    }
    public static String getUrlName(String url){
        return urlNameMap.get(url);
    }


    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }




}