package com.zph.programmer.springboot.config;


import com.zph.programmer.springboot.filter.HttpServletWrapperFilter;
import com.zph.programmer.springboot.interceptor.OnlineInterceptor;
import com.zph.programmer.springboot.listener.OnlineListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    /**
     * 注册过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean<HttpServletWrapperFilter> filterRegistry() {
        FilterRegistrationBean<HttpServletWrapperFilter> frBean = new FilterRegistrationBean<>();
        frBean.setFilter(new HttpServletWrapperFilter());
        frBean.setOrder(1);//多个过滤器时指定过滤器的执行顺序
        frBean.addUrlPatterns("/*");
        return frBean;
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OnlineInterceptor()).addPathPatterns("/**");
    }

    /**
     * 注册监听器
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<OnlineListener> listenerRegistry() {
        ServletListenerRegistrationBean<OnlineListener> srb = new ServletListenerRegistrationBean<>();
        srb.setListener(new OnlineListener());
        return srb;
    }






}