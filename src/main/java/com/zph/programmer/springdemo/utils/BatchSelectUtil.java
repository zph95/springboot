package com.zph.programmer.springdemo.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zengpenghui
 * @Date: 2020/5/28 11:18
 * @Version 1.0
 */
public class BatchSelectUtil {

    private final static int BATCH_SIZE = 200;

    public static <T> List<List<T>> splitList(List<T> list) {
        return splitList(list, BATCH_SIZE);
    }

    public static <T> List<List<T>> splitList(List<T> list, Integer batchSize) {
        if (batchSize == null || batchSize <= 0) {
            batchSize = BATCH_SIZE;
        }
        list = list.stream().distinct().collect(Collectors.toList());
        List<List<T>> splitList = new ArrayList<>();
        int total = list.size();
        int totalBatch = (total + batchSize - 1) / batchSize;
        for (int i = 0; i < totalBatch; i++) {
            int startIndex = i * batchSize;
            int endIndex = startIndex + batchSize;
            endIndex = Math.min(endIndex, total);
            splitList.add(list.subList(startIndex, endIndex));
        }
        return splitList;
    }
}
