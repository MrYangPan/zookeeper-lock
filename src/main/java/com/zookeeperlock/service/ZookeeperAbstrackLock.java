package com.zookeeperlock.service;

import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Mr.PanYang on 2018/8/21.
 * <p>
 * 重构重复代码，将重复代码交给子类执行
 */
public abstract class ZookeeperAbstrackLock implements ILock {

    private static final String CONNECTSTRING = "127.0.0.1:2181";
    protected ZkClient zkClient = new ZkClient(CONNECTSTRING);
    protected static final String PATH = "/lock";
    //信号量，线程等待执行
    protected CountDownLatch countDownLatch = null;

    @Override
    public void getLock() {
        if (tryLock()) {
            System.out.println("获取锁成功.........");
        } else {
            //等待
            waitLock();
            // 重新获取锁
            getLock();
        }
    }

    // 是否获取锁成功
    abstract boolean tryLock();

    //等待
    abstract void waitLock();

    //释放锁
    @Override
    public void unLock() {
        if (zkClient != null) {
            System.out.println("关闭连接");
            zkClient.close();
        }
    }
}
