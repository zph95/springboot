package com.zph.programmer.springboot.cache;

public interface CacheService {

    class keyValue{
        public String key;
        public String value;
    }

    String get(String key)throws Exception;

    void set(String key,String value)throws Exception;

    void del(String key)throws Exception;

}
