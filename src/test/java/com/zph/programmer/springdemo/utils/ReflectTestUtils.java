package com.zph.programmer.springdemo.utils;

import org.springframework.aop.support.AopUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 代替@InjectMock，因@InjectMock 会造成spring bean环境污染
 * 此方法先得到原来的值，并保存
 * 在完成test case后，调用revert()，还原spring bean环境
 */
public class ReflectTestUtils {

    private static Map<K, Object> targetField2OriginFieldObject = new HashMap<>(16);

    public static void setField(Object targetObject, String fieldName, Object value) {
        //若为Aop，取出真正的实例
        if (AopUtils.isAopProxy(targetObject)) {
            targetObject = AopUtils.getTargetClass(targetObject);
        }
        Object originFieldObject = ReflectionTestUtils.getField(targetObject, fieldName);
        K k = new K();
        k.setField(fieldName);
        k.setTarget(targetObject);
        targetField2OriginFieldObject.put(k, originFieldObject);
        ReflectionTestUtils.setField(targetObject, fieldName, value);
    }

    public static void revert() {
        for (Map.Entry<K, Object> e : targetField2OriginFieldObject.entrySet()) {
            ReflectionTestUtils.setField(e.getKey().getTarget(), e.getKey().getField(), e.getValue());
        }
        targetField2OriginFieldObject = new HashMap<>(16);
    }

    public static class K {
        private Object target;
        private String field;

        public Object getTarget() {
            return target;
        }

        public void setTarget(Object target) {
            this.target = target;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            K k = (K) o;
            return Objects.equals(target, k.target) &&
                    Objects.equals(field, k.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(target, field);
        }
    }
}
