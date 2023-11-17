package com.zph.programmer.springdemo.utils;

import org.apache.ibatis.parsing.GenericTokenParser;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.parsing.TokenHandler;




public class MybatisGenericUtil {

    GenericTokenParser parser;

    MybatisGenericUtil(Map<String,Object> variables){

        parser = new GenericTokenParser("#{", "}", new TokenHandler() {
            @Override
            public String handleToken(String content) {
                Object value = variables.get(content);
                if(value instanceof String){
                    return "'"+value+"'";
                }
                else{
                    return value.toString();
                }
            }
        });
    }

    public String parseSql(String preSql){
        return parser.parse(preSql);
    }

    public static void main(String[] args) {
        String preSql = "select * from user where user_id = #{userId}";
        Map<String,Object> variables = new HashMap<>();
        variables.put("userId",1);
        MybatisGenericUtil genericUtil =  new MybatisGenericUtil(variables);
        System.out.println(genericUtil.parseSql(preSql));
    }

}
