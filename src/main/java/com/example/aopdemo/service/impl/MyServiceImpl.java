package com.example.aopdemo.service.impl;

import com.example.aopdemo.annotation.DailyExecution;
import com.example.aopdemo.annotation.LimitCalling;
import com.example.aopdemo.service.MyService;
import org.springframework.stereotype.Service;


@Service
public class MyServiceImpl implements MyService {
    @DailyExecution
    @Override
    public boolean updateLoginTime(String userId) {
        System.out.println("刷新用户" + userId + "的登录时间");
        return true;
    }

    @LimitCalling(toleration = 5, freezePeriod = 50)
    @Override
    public boolean checkPhone(String phone) {
        return phone != null && phone.length() > 4 && phone.endsWith("1234");
    }
}
