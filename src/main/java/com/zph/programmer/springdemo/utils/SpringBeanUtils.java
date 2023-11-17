package com.zph.programmer.springdemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SpringBeanUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * 获取应用上下文并获取相应的接口实现类
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        SpringBeanUtils.context = applicationContext;

    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, String name) {
        return context.getBean(name, clazz);
    }


    /**
     * 获取接口的实现类实例。
     *
     * @param clazz
     * @return
     */
    public static <T> Map<String, T> getImplInstance(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }

    public static <T> List<T> getImplInstanceArray(Class<T> clazz) {

        Map<String, T> map = context.getBeansOfType(clazz);

        return new ArrayList<>(map.values());
    }


    /**
     * 发布事件。
     *
     * @param event void
     */
    public static void publishEvent(ApplicationEvent event) {
        if (context != null) {
            context.publishEvent(event);
        }
    }
}