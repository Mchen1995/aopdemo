package com.example.aopdemo.controller;

import com.example.aopdemo.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @Autowired
    private MyService myService;

    @GetMapping("greet")
    public boolean greet() {
        String userId = "891421";
        return myService.methodWithCacheMode2(userId);  // 方法被代理拦截后，其返回值是代理的返回值
    }
}
