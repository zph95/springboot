package com.zph.programmer.springboot.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ZengPengHui at 2021/9/7.
 */
public class MultiTaggedCounter {
    private String name;
    private String[] tagNames;
    private MeterRegistry registry;
    private Map<String, Counter> counterMap = new ConcurrentHashMap<>();

    public MultiTaggedCounter(String name, MeterRegistry registry, String... tags) {
        this.name = name;
        this.tagNames = tags;
        this.registry = registry;
    }

    public void increment(String... tagValues) {
        String valuesString = Arrays.toString(tagValues);
        if (tagValues.length != tagNames.length) {
            throw new IllegalArgumentException("Counter tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + valuesString);
        }
        Counter counter = counterMap.get(valuesString);
        if (counter == null) {
            List<Tag> tags = new ArrayList<>(tagNames.length);
            for (int i = 0; i < tagNames.length; i++) {
                tags.add(new ImmutableTag(tagNames[i], tagValues[i]));
            }
            counter = Counter.builder(name).tags(tags).register(registry);
            counterMap.put(valuesString, counter);
        }
        counter.increment();
    }

}
