package com.example.aopdemo.service;

public interface MyService {

    /**
     * 更新用户的登录时间，该方法会被拦截，保证每个用户每天最多更新一次
     * @param userId 用户id
     * @return 若方法被拦截，返回false，否则返回true
     */
    boolean updateLoginTime(String userId);
}
