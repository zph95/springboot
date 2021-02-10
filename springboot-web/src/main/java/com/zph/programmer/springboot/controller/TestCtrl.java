package com.zph.programmer.springboot.controller;

import com.zph.programmer.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/test",name = "测试")
public class TestCtrl {

    @Autowired
    private TestService testService;

    @GetMapping(value = "/testLog")
    public Map<String,String> testControllerLog(@RequestParam("param") String param){
        return testService.testServiceLog(param);
    }
}
