package com.zph.programmer.springdemo.controller;

import com.zph.programmer.springdemo.dto.UserInfoDto;
import com.zph.programmer.springdemo.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @Author: zengpenghui
 * @Date: 2021/2/25 18:59
 * @Version 1.0
 */
@Validated
@RestController
@RequestMapping(value = "/", name = "注册用户")
public class UserInfoCtrl {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/getUser")
    public String getUserStr(@NotNull(message = "name 不能为空") String name,
                             @Max(value = 99, message = "不能大于99岁") Integer age) {
        return "name: " + name + " ,age:" + age;
    }

    @PostMapping("/register")
    public void doRegister(@RequestBody @Validated UserInfoDto userInfoDto, HttpServletResponse response) throws IOException {
        boolean insert = userInfoService.insert(userInfoDto);
        if (insert) {
            response.sendRedirect("sign?success");
        } else {
            response.sendRedirect("sign?error");
        }
    }
}