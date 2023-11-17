package com.zph.programmer.springdemo.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: zengpenghui
 * @Date: 2021/3/11 15:34
 * @Version 1.0
 */
@Target({ANNOTATION_TYPE, METHOD, FIELD})
@Retention(RUNTIME)
@Documented
//此处指定了注解的实现类为ListNotHasNullValidatorImpl
@Constraint(validatedBy = ListNotHasNullValidatorImpl.class)
public @interface ListNotHasNull {

    /**
     * 添加value属性，可以作为校验时的条件,若不需要，可去掉此处定义
     */
    int value() default 0;

    String message() default "List集合中不能含有null元素";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 定义List，为了让Bean的一个属性上可以添加多套规则
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ListNotHasNull[] value();
    }
}