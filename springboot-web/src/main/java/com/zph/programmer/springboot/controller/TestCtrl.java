package com.zph.programmer.springboot.controller;

import com.zph.programmer.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zph.programmer.springboot.annotation.ControllerLog;

@RestController
@RequestMapping("/test")
public class TestCtrl {

    @Autowired
    private TestService testService;

    @GetMapping("/testLog")
    @ControllerLog("测试注解controllerLog")
    public String testControllerLog(@RequestParam("param") String param){
        return testService.testServiceLog(param);
    }
}
