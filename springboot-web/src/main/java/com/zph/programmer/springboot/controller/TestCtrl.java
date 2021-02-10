package com.zph.programmer.springboot.controller;

import com.zph.programmer.springboot.cache.CacheService;
import com.zph.programmer.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/test", name = "测试")
public class TestCtrl {

    @Autowired
    private final TestService testService;
    @Autowired
    private final CacheService cacheService;

    public TestCtrl(TestService testService, CacheService cacheService) {
        this.testService = testService;
        this.cacheService = cacheService;
    }

    @GetMapping(value = "/testLog", name = "测试controller日志")
    public Map<String, String> testControllerLog(@RequestParam("param") String param) throws Exception {

        return testService.testPointLog(param);
    }

    @GetMapping(value = "/testCache", name = "测试Cache get方法")
    public String testCacheGet(@RequestParam("key") String key) throws Exception {

        return cacheService.get(key);

    }

    @PostMapping(value = "/testCache", name = "测试Cache set方法")
    public void testCachePost(@RequestBody CacheService.KeyValue keyValue) throws Exception {
        cacheService.set(keyValue.getKey(), keyValue.getValue());
    }

    @PutMapping(value = "/testCache", name = "测试Cache put方法")
    public void testCachePut(@RequestBody CacheService.KeyValue keyValue) throws Exception {

        cacheService.set(keyValue.getKey(), keyValue.getValue());

    }

    @DeleteMapping(value = "/testCache", name = "测试Cache delete方法")
    public void testCacheDelete(@RequestParam("key") String key) throws Exception {
        cacheService.del(key);

    }

}
