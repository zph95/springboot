package com.zph.programmer.springboot.service;

import com.zph.programmer.springboot.po.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.ArrayList;
import java.util.List;
/**
 * @Author: zengpenghui
 * @Date: 2021/2/25 19:09
 * @Version 1.0
 */
@Service
public class UserDetailsServiceNew implements UserDetailsService{
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoService.selectUserInfo(userName);
        if (userInfo == null) {
            throw new UsernameNotFoundException("用户不存在"); // 若不存在抛出用户不存在异常
        }
        // 权限字符串转化
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        String[] roles = userInfo.getUserRoles().split(",");// 获取后的Roles必须有ROLE_前缀，否则会抛Access is denied无权限异常
        for (String role : roles) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        // 交给security进行验证并返回
        return new User(userInfo.getUserName(), userInfo.getUserPassword(), simpleGrantedAuthorities);
    }
}

