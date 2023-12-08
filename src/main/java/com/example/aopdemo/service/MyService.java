package com.example.aopdemo.service;

import java.util.Date;

public interface MyService {

    /**
     * 方法3，使用缓存，缓存过期时间为指定时刻
     */
    boolean methodWithCacheMode2(String userId);
}
