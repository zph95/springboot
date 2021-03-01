package com.zph.programmer.springboot.controller;

import com.zph.programmer.springboot.po.UserInfo;
import com.zph.programmer.springboot.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zengpenghui
 * @Date: 2021/2/25 18:59
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/", name = "注册用户")
public class UserInfoCtrl{

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/register")
    public void doRegister(UserInfo userInfo, HttpServletResponse response) throws IOException {
        boolean insert = userInfoService.insert(userInfo);
        if (insert) {
            response.sendRedirect("sign?success");
        } else {
            response.sendRedirect("sign?error");
        }
    }
}