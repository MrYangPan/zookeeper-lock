package com.zookeeperlock.service;

/**
 * Created by Mr.PanYang on 2018/8/21.
 * <p>
 * 定义分布式锁
 */
public interface ILock {
    // 获取锁
    void getLock();

    // 释放锁
    void unLock();
}
