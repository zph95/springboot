package com.zph.programmer.springboot.controller;

import com.zph.programmer.api.dto.BaseResponseDto;
import com.zph.programmer.springboot.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/test", name = "测试")
public class TestCtrl {

    @Autowired
    private final CacheService cacheService;

    public TestCtrl(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping(value = "/testCache", name = "测试Cache get方法")
    public BaseResponseDto<String> testCacheGet(@RequestParam("key") String key) throws Exception {
        return BaseResponseDto.success(HttpStatus.OK.value(), cacheService.get(key));
    }

    @PostMapping(value = "/testCache", name = "测试Cache set方法")
    public BaseResponseDto<String> testCachePost(@RequestBody CacheService.KeyValue keyValue) throws Exception {
        cacheService.set(keyValue.getKey(), keyValue.getValue());
        return BaseResponseDto.success(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase());
    }

    @PutMapping(value = "/testCache", name = "测试Cache put方法")
    public BaseResponseDto<String> testCachePut(@RequestBody CacheService.KeyValue keyValue) throws Exception {
        cacheService.set(keyValue.getKey(), keyValue.getValue());
        return BaseResponseDto.success(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase());
    }

    @DeleteMapping(value = "/testCache", name = "测试Cache delete方法")
    public BaseResponseDto<String> testCacheDelete(@RequestParam("key") String key) throws Exception {
        cacheService.del(key);
        return BaseResponseDto.success(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase());
    }
}
