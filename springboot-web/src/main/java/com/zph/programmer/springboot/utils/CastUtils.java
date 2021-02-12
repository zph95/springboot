package com.zph.programmer.springboot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 这个工具仅仅为了不显示waring
 * Unchecked cast: 'java.lang.Object' to 'java.util.Map<java.lang.String,java.lang.Object>'
 * Unchecked cast: 'java.lang.Object' to 'java.util.List<java.lang.Object>'
 */
public class CastUtils {
    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    public static <K,V> Map<K,V> castMap(Object obj, Class<K> clazzK,Class<V> clazzV){
        Map<K,V> result=new HashMap<>();
        if(obj instanceof Map<?,?>){
            Map<?,?> tmpMap=((Map<?,?>) obj);
            for(Object key:tmpMap.keySet()){
                Object value = tmpMap.get(key);
                result.put(clazzK.cast(key),clazzV.cast(value));
            }
            return result;
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        list.add("src/main/profile/test");
        Object objList = list;
        List<String> arrys = castList(objList, String.class);
        for (String once : arrys) {
            System.out.println(once);
        }

        Map<String,String> map=new HashMap<>();
        map.put("test-Key","test-value");
        Object objMap = map;
        Map<String,String>  result = castMap(objMap, String.class,String.class);
        for (Map.Entry<String,String> entry : result.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}
