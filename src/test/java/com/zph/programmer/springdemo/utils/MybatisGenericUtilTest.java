package com.zph.programmer.springdemo.utils;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;

import com.zph.programmer.springdemo.SpringdemoApplication;

class MybatisGenericUtilTest extends SpringdemoApplication{

    @Test
    void testParse(){
        String preSql = "select * from user where user_id = #{userId}";
        Map<String,Object> variables = new HashMap<>();
        variables.put("userId",1);
        MybatisGenericUtil genericUtil =  new MybatisGenericUtil(variables);
        String result =  genericUtil.parseSql(preSql);
        Assertions.assertEquals("select * from user where user_id = 1", result);
    }
}
