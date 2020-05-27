package com.example.springbootredislock.springbootredislock.common;

import com.example.springbootredislock.springbootredislock.common.DistributedLocker;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description:  Lock接口实现类
 * @Author: zhoufu
 * @Date: 15:55 2020/5/26
 */
@Component
public class RedisDistributedLocker implements DistributedLocker {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public RLock lock(String lockKey) {
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.lock();
        return rLock;
    }

    @Override
    public RLock lock(String lockKey, int leaseTime) {
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.lock(leaseTime, TimeUnit.SECONDS);
        return rLock;
    }

    @Override
    public RLock lock(String lockKey, TimeUnit unit, int timeout) {
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.lock(timeout, unit);
        return rLock;
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            return rLock.tryLock(waitTime, leaseTime, unit);
        }catch (InterruptedException  e){
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.unlock();
    }

    @Override
    public void unlock(RLock lock) {
    lock.unlock();
    }
}
