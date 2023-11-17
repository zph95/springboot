package com.zph.programmer.springdemo.config;

/**
 * @Author: zengpenghui
 * @Date: 2021/2/24 9:55
 * @Version 1.0
 */
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
        // 新增注册页面
        registry.addViewController("/sign").setViewName("register");
        registry.addViewController("/user").setViewName("user/user");

    }

}