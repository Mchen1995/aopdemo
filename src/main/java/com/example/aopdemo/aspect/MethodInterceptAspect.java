package com.example.aopdemo.aspect;

import com.example.aopdemo.annotation.LimitCalling;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * 方法拦截切面类
 */
@Aspect
@Component
public class MethodInterceptAspect {
    /**
     * 保存用户的登录时间
     */
    private final Map<String, Date> userLoginTimeMap;

    /**
     * 错误次数
     */
    private int errCount;

    /**
     * 上次校验时间
     */
    private LocalDateTime lastCheckTime;

    public MethodInterceptAspect(Map<String, Date> userLoginTimeMap) {
        this.userLoginTimeMap = userLoginTimeMap;
    }

    @Around("@annotation(com.example.aopdemo.annotation.LimitCalling)")
    public boolean handleCallingLimitation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LimitCalling annotation = signature.getMethod().getAnnotation(LimitCalling.class);
        if (isFrozen(annotation.toleration(), annotation.freezePeriod())) {
            throw new Exception("请"+annotation.freezePeriod()+"秒后再试");
        } else {
            boolean checkPass = (boolean) joinPoint.proceed();
            updateErrCount(checkPass);
            return checkPass;
        }
    }

    /**
     * 根据错误次数计数，判断方法是否被冻结
     *
     * @return true-被冻结，false-未被冻结
     */
    private boolean isFrozen(int toleration, int freezePeriod) {
        if (errCount >= toleration) {
            if (lastCheckTime == null) return false;
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(lastCheckTime, now);
            long seconds = duration.getSeconds();
            return seconds < freezePeriod;
        }
        return false;
    }

    /**
     * 根据校验结果更新错误计数
     *
     * @param checkPass 是否校验通过
     */
    private void updateErrCount(boolean checkPass) {
        if (checkPass) errCount = 0;
        else {
            errCount += 1;
            lastCheckTime = LocalDateTime.now();
        }
    }

    /**
     * Around advice 是最强大的 advice，可以在方法执行前后做自定义操作，可以修改方法的参数和返回值，可以跳过方法的执行
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.example.aopdemo.annotation.DailyExecution)")
    public boolean beforeDailyExec(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String userId = (String) args[0];  // 获取方法的第一个参数作为判断依据

        if (this.shouldUpdateLoginTimeMap(userId)) {
            System.out.println("满足执行条件，当前时间" + new Date() + "，上次登录时间" + userLoginTimeMap.get(userId));
            this.userLoginTimeMap.put(userId, new Date());
            return (boolean) joinPoint.proceed();  // jointPoint 作为代理代为执行目标方法
        } else {
            System.out.println("不满足执行条件，当前时间" + new Date() + "，上次登录时间" + userLoginTimeMap.get(userId));
            this.userLoginTimeMap.put(userId, new Date());
            return false;
        }
    }

    /**
     * 是否应该刷新用户登录时间
     *
     * @param userId 用户ID
     * @return true-应该；false-不应该
     */
    private boolean shouldUpdateLoginTimeMap(String userId) {
        Date lastLoginTime = this.userLoginTimeMap.get(userId);
        if (lastLoginTime == null) {
            System.out.println("未查到内存中用户" + userId + "的登录记录");
            return true;  // 如果内存中没有该用户的登录记录，则应该刷新
        }

        Date now = new Date();
        LocalDate localDateLastLogin
                = lastLoginTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateNow
                = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return !localDateNow.equals(localDateLastLogin);  // 如果当前时间和用户上一次登录时间不属于同一天，则应该刷新
    }
}
