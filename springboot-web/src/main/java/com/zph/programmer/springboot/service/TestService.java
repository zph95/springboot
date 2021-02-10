package com.zph.programmer.springboot.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.zph.programmer.springboot.annotation.PointLog;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TestService {

    @PointLog("测试注解@PointLog")
    public Map<String,String> testServiceLog(String param) {
        log.info("param={}", param);
        Map<String,String> result=new HashMap<>();
        result.put("200","success");
        return result;
    }
}
