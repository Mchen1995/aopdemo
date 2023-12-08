package com.example.aopdemo.service.impl;

import com.example.aopdemo.annotation.DailyExecution;
import com.example.aopdemo.service.MyService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MyServiceImpl implements MyService {
    @DailyExecution
    @Override
    public boolean methodWithCacheMode2(String userId) {
        System.out.println("刷新用户" + userId + "的登录时间");
        return true;
    }
}
