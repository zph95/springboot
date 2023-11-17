package com.zph.programmer.springdemo.monitor;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by ZengPengHui at 2021/9/7.
 */
public class MultiTaggedTimer {
    private String name;
    private String[] tagNames;
    private MeterRegistry registry;
    private Map<String, Timer> timerMap = new ConcurrentHashMap<>();

    public MultiTaggedTimer(String name, MeterRegistry registry, String... tags) {
        this.name = name;
        this.tagNames = tags;
        this.registry = registry;
    }

    public Timer getTimer(String... tagValues) {
        String valuesString = Arrays.toString(tagValues);
        if (tagValues.length != tagNames.length) {
            throw new IllegalArgumentException("Timer tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + valuesString);
        }
        Timer timer = timerMap.get(valuesString);
        if (timer == null) {
            List<Tag> tags = new ArrayList<>(tagNames.length);
            for (int i = 0; i < tagNames.length; i++) {
                tags.add(new ImmutableTag(tagNames[i], tagValues[i]));
            }
            timer = Timer.builder(name).tags(tags).register(registry);
            timerMap.put(valuesString, timer);
        }
        return timer;
    }
}
