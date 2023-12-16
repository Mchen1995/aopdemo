package com.example.aopdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限制方法调用频率的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitCalling {

    /**
     * 冻结时间 默认 60 秒
     */
    int freezePeriod() default 60;

    /**
     * 容忍错误次数 默认 3 次
     */
    int toleration() default 3;
}
