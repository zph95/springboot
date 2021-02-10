package com.zph.programmer.springboot.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringBeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 获取应用上下文并获取相应的接口实现类
     *
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        SpringBeanUtils.applicationContext = applicationContext;

    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }




}