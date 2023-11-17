package com.zph.programmer.springdemo.dao;

import org.apache.ibatis.annotations.Param;

import com.zph.programmer.springdemo.po.UserInfo;

public interface UserInfoMapper {
    int insert(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(UserInfo record);

    /**
     * 查询
     */
    UserInfo selectByUserName(@Param("userName") String userName);
}