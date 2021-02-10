package com.zph.programmer.springboot.cache;

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

    String get(String key)throws Exception;

    void set(String key,String value)throws Exception;

    void del(String key)throws Exception;

}
