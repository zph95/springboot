package com.zph.programmer.springdemo.cache;

import com.zph.programmer.springdemo.exception.CacheException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CacheService {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class KeyValue {
        private String key;
        private String value;
    }

    String get(String key) throws CacheException;

    void set(String key, String value, int second) throws CacheException;

    void del(String key) throws CacheException;

}
