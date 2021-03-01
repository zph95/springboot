package com.zph.programmer.springboot.config;

/**
 * @Author: zengpenghui
 * @Date: 2021/2/24 10:01
 * @Version 1.0
 */

import com.zph.programmer.springboot.service.UserDetailsServiceNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceNew userDetailsServiceNew;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index").permitAll() // permitAll被允许访问
                .antMatchers("/user/**").hasRole("USER")// 指定所有user页面需要USER角色才能访问
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/user")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication() // 在内存中进行身份验证
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("user")
//                .password(new BCryptPasswordEncoder().encode("123456"))
//                .roles("USER");

        // 将内存验证改为获取数据库，并使用了密码加密
        auth.userDetailsService(userDetailsServiceNew).passwordEncoder(new BCryptPasswordEncoder());
    }
}