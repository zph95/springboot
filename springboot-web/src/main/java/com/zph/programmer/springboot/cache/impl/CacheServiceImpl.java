package com.zph.programmer.springboot.cache.impl;

import com.zph.programmer.springboot.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    private final static String cacheName="spring-boot-demo-cache";
    @Autowired
    private CacheManager cacheManager;

    @Override
    public String get(String key)throws Exception {
        Cache cache = cacheManager.getCache(cacheName);
        if(cache!=null) {
            return cache.get(key, String.class);
        }
        else{
            throw new Exception("缓存"+cacheName+"异常！");
        }
    }

    @Override
    public void set(String key, String value)throws Exception {
        Cache cache = cacheManager.getCache(cacheName);
        if(cache!=null) {
            cache.put(key, value);
        }
        else{
            throw new Exception("缓存"+cacheName+"异常！");
        }
    }

    @Override
    public void del(String key) throws Exception{
        Cache cache = cacheManager.getCache(cacheName);
        if(cache!=null) {
            cache.evict(key);
        }
        else{
            throw new Exception("缓存"+cacheName+"异常！");
        }
    }
}
