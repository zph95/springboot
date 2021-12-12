package com.zph.programmer.springboot.monitor;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ZengPengHui at 2021/9/7.
 */

public class MultiTaggedGauge {
    private String name;
    private String[] tagNames;
    private MeterRegistry registry;
    private Map<String, DoubleWrapper> gaugeMap = new ConcurrentHashMap<>();

    public MultiTaggedGauge(String name, MeterRegistry meterRegistry, String... tagNames) {
        this.name = name;
        this.tagNames = tagNames;
        this.registry = meterRegistry;
    }

    public void setGauge(Double value, String... tagValues) {
        String valuesString = Arrays.toString(tagValues);
        if (tagValues.length != tagNames.length) {
            throw new IllegalArgumentException("Counter tags mismatch! Expected args are " + Arrays.toString(tagNames) + ", provided tags are " + valuesString);
        }
        DoubleWrapper number = gaugeMap.get(valuesString);
        if (number == null) {
            List<Tag> tags = new ArrayList<>(tagNames.length);
            for (int i = 0; i < tagNames.length; i++) {
                tags.add(new ImmutableTag(tagNames[i], tagValues[i]));
            }
            DoubleWrapper valueHolder = new DoubleWrapper(value);
            Gauge.builder(name, valueHolder, DoubleWrapper::getValue).tags(tags).register(registry);
            gaugeMap.put(valuesString, valueHolder);
        } else {
            number.setValue(value);
        }
    }

    public void removeNotExistGauge(List<List<String>> tagValueList) {
        Set<String> valueStringSet = new HashSet<>();
        for (List<String> tagValues : tagValueList) {
            valueStringSet.add(Arrays.toString(tagValues.toArray()));
        }
        Iterator<Map.Entry<String, DoubleWrapper>> iterator = gaugeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, DoubleWrapper> next = iterator.next();
            if (!valueStringSet.contains(next.getKey())) {
                next.getValue().setValue(0L);
                iterator.remove();
            }
        }
    }
}
