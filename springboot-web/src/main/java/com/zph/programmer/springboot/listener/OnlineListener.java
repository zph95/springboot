package com.zph.programmer.springboot.listener;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @description:  ServletRequestListener、 HttpSessionListener 、ServletContextListener ......
 * 可直接在MyListener类上使用@WebListener注解,入口类加上@ServletComponentScan即可
 */
@Slf4j
public class OnlineListener implements HttpSessionListener {

    public static int online = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        online ++;
        log.info("online在线人数为：" + online);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}
