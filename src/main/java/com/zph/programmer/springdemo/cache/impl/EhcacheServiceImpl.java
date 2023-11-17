package com.zph.programmer.springdemo.cache.impl;


import com.zph.programmer.springdemo.cache.CacheService;
import com.zph.programmer.springdemo.exception.CacheException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

@Service("ehcache")
public class EhcacheServiceImpl implements CacheService {
    private final static String ehcacheName = "ehcache";
    @Autowired
    private EhCacheCacheManager ehCacheCacheManager;

    @Override
    public String get(String key) throws CacheException {
        Cache cache = ehCacheCacheManager.getCacheManager().getCache(ehcacheName);
        if (cache != null) {
            Element element = cache.get(key);
            return element == null ? null : String.valueOf(element.getObjectValue());
        } else {
            throw new CacheException("缓存" + ehcacheName + "异常！");

        }
    }

    @Override
    public void set(String key, String value, int second) throws CacheException {
        Cache cache = ehCacheCacheManager.getCacheManager().getCache(ehcacheName);
        if (cache != null) {

            Element element = new Element(key, value, second, second);

            /**
             * element
             * timeToLiveSeconds=x：缓存自创建日期起至失效时的间隔时间x；
             *
             * timeToIdleSeconds=y：缓存创建以后，最后一次访问缓存的日期至失效之时的时间间隔y；
             * 若自创建缓存后一直都没有访问缓存，那么间隔x后失效，若自创建缓存后有N次访问缓存，那么计算（最后一次访问缓存时间+y ） 即：按照timeToIdleSeconds计算，但总存活时间不超过 y;举个例子：
             *
             * timeToIdleSeconds=120；
             *
             * timeToLiveSeconds=180；
             *
             * 上面的表示此缓存最多可以存活3分钟，如果期间超过2分钟未访问 那么此缓存失效！
             */
            cache.put(element);
        } else {
            throw new CacheException("缓存" + ehcacheName + "异常！");
        }
    }

    @Override
    public void del(String key) throws CacheException {
        Cache cache = ehCacheCacheManager.getCacheManager().getCache(ehcacheName);
        if (cache != null) {
            cache.remove(key);
        } else {
            throw new CacheException("缓存" + ehcacheName + "异常！");

        }
    }
}
