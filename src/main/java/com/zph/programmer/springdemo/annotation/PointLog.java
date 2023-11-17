package com.zph.programmer.springdemo.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PointLog {

    String value() default "";
}
