package com.zph.programmer.springboot.listener;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by ZengPengHui at 2021/11/15.
 */
public class EventListener {
    /**
     * 在 Guava EventBus 中，是根据参数类型进行订阅，每个订阅的方法只能由一个参数，同时需要使用 @Subscribe 标识
     */

    /**
     * 监听 Integer 类型的消息
     */
    @Subscribe
    public void listenInteger(Integer param) {
        System.out.println("EventListener#listenInteger ->" + param);
    }

    /**
     * 监听 String 类型的消息
     */
    @Subscribe
    @AllowConcurrentEvents
    public void listenString1(String listenSeq) {

        System.out.println(LocalDateTime.now()+" step start 1->" + listenSeq);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LocalDateTime.now()+" step end 1->" + listenSeq);

    }

    @Subscribe
    @AllowConcurrentEvents
    public void listenString2(String listenSeq) {

        System.out.println(LocalDateTime.now()+" step start 2->" + listenSeq);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LocalDateTime.now()+" step end 2->" + listenSeq);

    }

    @Subscribe
    @AllowConcurrentEvents
    public void listenString3(String listenSeq) {

        System.out.println(LocalDateTime.now()+" step start 3->" + listenSeq);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LocalDateTime.now()+" step end 3->" + listenSeq);

    }

    @Subscribe
    @AllowConcurrentEvents
    public void listenString4(String listenSeq) {

        System.out.println(LocalDateTime.now()+" step start 4->" + listenSeq);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LocalDateTime.now()+" step end 4->" + listenSeq);

    }

    @Subscribe
    @AllowConcurrentEvents
    public void listenString5(String listenSeq) {

        System.out.println(LocalDateTime.now()+" step start 5->" + listenSeq);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LocalDateTime.now()+" step end 5->" + listenSeq);

    }


}
