package com.zph.programmer.springboot.controller;

import com.zph.programmer.springboot.cache.CacheService;
import com.zph.programmer.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/test",name = "测试")
public class TestCtrl {

    @Autowired
    private TestService testService;
    @Autowired
    private CacheService cacheService;

    @GetMapping(value = "/testLog",name = "测试controller日志")
    public Map<String,String> testControllerLog(@RequestParam("param") String param){
        try {
            return testService.testPointLog(param);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping(value = "/testCache",name = "测试Cache get方法")
    public String testCacheGet(@RequestParam("key") String key){
        try {
            return cacheService.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(value = "/testCache",name = "测试Cache set方法")
    public void testCachePost(@RequestBody CacheService.keyValue keyValue){
        try {
             cacheService.set(keyValue.key,keyValue.value);
        } catch (Exception e) {
            return;
        }
    }

    @PutMapping(value = "/testCache",name = "测试Cache put方法")
    public void testCachePut(@RequestBody CacheService.keyValue keyValue){
        try {
            cacheService.set(keyValue.key,keyValue.value);
        } catch (Exception e) {
            return;
        }
    }

    @DeleteMapping(value = "/testCache",name = "测试Cache delete方法")
    public void testCacheDelete(@RequestParam("key") String key){
        try {
            cacheService.del(key);
        } catch (Exception e) {
            return;
        }
    }

}
