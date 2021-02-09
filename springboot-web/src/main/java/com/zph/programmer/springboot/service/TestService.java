package com.zph.programmer.springboot.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.zph.programmer.springboot.annotation.ServiceLog;

@Slf4j
@Component
public class TestService {

    @ServiceLog("测试注解@serviceLog")
    public String testServiceLog(String param) {
        log.info("param={}", param);
        return "success";
    }
}
