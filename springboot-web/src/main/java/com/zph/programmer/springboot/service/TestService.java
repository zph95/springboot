package com.zph.programmer.springboot.service;
import com.zph.programmer.springboot.dao.RestCallLogRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.zph.programmer.springboot.annotation.PointLog;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TestService {


    @PointLog("测试注解@PointLog")
    public Map<String,String> testPointLog(String param) throws Exception{
        if(param==null){
            throw new Exception("参数为空");
        }
        else{
            log.info("param={}", param);
            Map<String,String> result=new HashMap<>();
            result.put("200","success");
            return result;
        }
    }
}
