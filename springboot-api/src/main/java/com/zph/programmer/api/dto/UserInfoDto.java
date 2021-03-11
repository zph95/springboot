package com.zph.programmer.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author: zengpenghui
 * @Date: 2021/3/10 10:50
 * @Version 1.0
 */
@Data
public class UserInfoDto {

    /**
     * 用户名
     */
    @NotNull
    @Size(min = 2, max = 30, message = "用户名长度应在2到30之间")
    private String userName;

    /**
     * 真实姓名
     */
    @NotNull(message = "realName cannot be null")
    private String realName;

    /**
     * 用户密码
     */
    @NotNull(message = "userPassword cannot be null")
    private String userPassword;
}
