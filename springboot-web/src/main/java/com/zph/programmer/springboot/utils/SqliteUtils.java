package com.zph.programmer.springboot.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqliteUtils {
    private static void createDateBase(String dbFileName) {
        try {
            //建立一个数据库名database.db的连接，如果不存在就在当前目录下创建之
            String url="jdbc:sqlite:"+dbFileName;
            Connection conn = DriverManager.getConnection(url);
            conn.close(); //结束数据库的连接
        } catch (Exception e) {
            log.error("创建sqlite数据库失败!",e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            //连接SQLite的JDBC
            Class.forName("org.sqlite.JDBC");
        }catch (Exception e){
            log.error("驱动未找到");
        }
        createDateBase("springboot-web/src/main/resources/static/sqlite/sqlite.db");
    }
}
