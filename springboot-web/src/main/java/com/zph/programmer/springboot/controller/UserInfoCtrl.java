package com.zph.programmer.springboot.controller;

import com.zph.programmer.springboot.po.UserInfo;
import com.zph.programmer.springboot.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @Author: zengpenghui
 * @Date: 2021/2/25 18:59
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/", name = "用户")
public class UserInfoCtrl{

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/register")
    public String doRegister(UserInfo userInfo){
        boolean insert = userInfoService.insert(userInfo);
        if (insert){
            return "redirect:sign?success";
        }
        return "redirect:sign?error";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal Principal principal, Model model){
        model.addAttribute("username", principal.getName());
        return "user/user";
    }
}