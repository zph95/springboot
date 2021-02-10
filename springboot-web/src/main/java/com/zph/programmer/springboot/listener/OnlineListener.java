package com.zph.programmer.springboot.listener;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *  ServletRequestListener、 HttpSessionListener 、ServletContextListener ......
 * 监听器
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
