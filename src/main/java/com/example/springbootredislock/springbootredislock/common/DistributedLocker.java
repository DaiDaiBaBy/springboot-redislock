package com.example.springbootredislock.springbootredislock.common;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 定义Lock的接口定义类  底层封装
 * @Author: zhoufu
 * @Date: 15:51 2020/5/26
 */
public interface DistributedLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, int timeout);

    RLock lock(String lockKey, TimeUnit unit, int timeout);

    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

}
