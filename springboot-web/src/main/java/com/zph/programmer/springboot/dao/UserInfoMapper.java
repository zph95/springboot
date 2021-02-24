package com.zph.programmer.springboot.dao;

import com.zph.programmer.springboot.po.UserInfo;

public interface UserInfoMapper {
    int insert(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(UserInfo record);
}