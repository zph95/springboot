package com.zph.programmer.springboot.listener;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.time.LocalDateTime;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.Assert.*;

public class EventListenerTest {

    @Test
    public void test() {
        EventBus eventBus = new EventBus();

        eventBus.register(new EventListener());

        eventBus.post(1);
        eventBus.post(2);


    }

    @Test
    public void testThreadEvent() throws InterruptedException {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(25);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("jvm-all-user-sensorsData:");
        executor.initialize();

        //EventBus eventBus = new AsyncEventBus(executor);
        EventBus eventBus = new EventBus();
        eventBus.register(new EventListener());
        Thread containA = new Thread(() -> {
            process("A",eventBus);
        });

        Thread containB = new Thread(() -> {
            process("B",eventBus);
        });
        Thread containC = new Thread(() -> {
            process("C",eventBus);
        });
        Thread containD = new Thread(() -> {
            process("D",eventBus);
        });
        Thread containE = new Thread(() -> {
            process("E",eventBus);
        });
        containA.start();
        containB.start();
        containC.start();
        containD.start();
        containE.start();
        Thread.sleep(100000000);
    }

    public void process(String listenSeq,  EventBus eventBus){
        if (false) {
            EventListener eventListener = new EventListener();
            eventListener.listenString1(listenSeq);
            eventListener.listenString2(listenSeq);
            eventListener.listenString3(listenSeq);
            eventListener.listenString4(listenSeq);
            eventListener.listenString5(listenSeq);
        } else {
            System.out.println(LocalDateTime.now()+" post start ->" + listenSeq);
            eventBus.post(listenSeq);
            System.out.println(LocalDateTime.now()+" post end ->" + listenSeq);
        }

    }
}