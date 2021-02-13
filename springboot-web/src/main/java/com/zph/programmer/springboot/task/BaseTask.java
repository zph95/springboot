package com.zph.programmer.springboot.task;

import lombok.extern.slf4j.Slf4j;
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
}
