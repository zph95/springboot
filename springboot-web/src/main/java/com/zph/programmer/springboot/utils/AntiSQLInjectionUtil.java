package com.zph.programmer.springboot.utils;

/**
 * @Author: zengpenghui
 * @Date: 2020/7/16 11:01
 * @Description: 防止SQL注入工具类
 */

import javax.servlet.http.HttpServletRequest;

public class AntiSQLInjectionUtil {

    /**
     * 过滤掉的sql关键字，可以手动添加
     */
    public final static String REGEX = "#|/\\*|\\*/|'|%|--|and|or|not|use|insert|delete|update|select|count|group|union"
            + "|create|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|source|sql";


    /**
     * 把SQL关键字替换为空字符串
     *
     * @param param
     * @return
     */
    public static String filter(String param) {
        if (param == null) {
            return param;
        }
        // (?i)不区分大小写替换
        return param.replaceAll("(?i)" + REGEX, "");
    }

    //效验
    protected static boolean sqlValidate(String str) {
        //统一转为小写
        str = str.toLowerCase();
        String[] badStrs = REGEX.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回经过防注入处理的字符串
     *
     * @param request
     * @param name
     * @return
     */
    public static String getParameter(HttpServletRequest request, String name) {
        return AntiSQLInjectionUtil.filter(request.getParameter(name));
    }

    public static void main(String[] args) {
        String str = "sElect * from test where id = 1 And name != 'sql' ";
        String outStr = "";
        for (int i = 0; i < 1000; i++) {
            outStr = AntiSQLInjectionUtil.filter(str);
        }
        System.out.println(outStr);
    }

}
