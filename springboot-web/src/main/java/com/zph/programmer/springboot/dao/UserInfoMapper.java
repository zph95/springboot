package com.zph.programmer.springboot.dao;

import com.zph.programmer.springboot.po.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserInfoMapper {
    int insert(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(UserInfo record);

    /**
     * 查询
     */
    UserInfo selectByUserName(@Param("userName") String userName);
}