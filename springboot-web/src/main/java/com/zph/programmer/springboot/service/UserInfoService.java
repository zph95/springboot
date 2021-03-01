package com.zph.programmer.springboot.service;

/**
 * @Author: zengpenghui
 * @Date: 2021/2/25 18:56
 * @Version 1.0
 */

import com.zph.programmer.springboot.dao.UserInfoMapper;
import com.zph.programmer.springboot.po.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * service业务层
 * 2018-12-10 16:37
 */
@Service
public class UserInfoService {

    /**
     * 获取后的Roles必须有ROLE_前缀，否则会抛Access is denied无权限异常
     */
    public static final String USER = "ROLE_USER";

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 新增用户
     *
     * @param userInfo
     * @return
     */
    public boolean insert(UserInfo userInfo) {
        UserInfo userInfo1 = userInfoMapper.selectByUserName(userInfo.getUserName());
        if (userInfo1 != null){
            return false;
        }
        // 加密保存密码到数据库
        userInfo.setUserRoles(USER);
        userInfo.setUserPassword(new BCryptPasswordEncoder().encode(userInfo.getUserPassword()));
        int result = userInfoMapper.insert(userInfo);
        return result == 1;
    }

    /**
     * 查询用户
     * @param username
     * @return
     */
    public UserInfo selectUserInfo(String username) {
        return userInfoMapper.selectByUserName(username);
    }

}