package com.zph.programmer.springdemo.task;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@PropertySource("classpath:cron.properties")
@Slf4j
@Component
public class BaseTask {

    @Scheduled(cron = "${job.base.task.cron}")
    public void doTask() {
        log.info("schedule,corn表达式");
    }

    // todo 未完成
    @PostConstruct
    public void init(){
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("ksession-rules");
        kieSession.fireAllRules();
        kieSession.dispose();
        System.out.println("规则执行完毕");
    }
}
