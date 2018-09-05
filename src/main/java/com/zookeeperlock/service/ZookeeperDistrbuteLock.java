package com.zookeeperlock.service;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Mr.PanYang on 2018/8/21.
 */
public class ZookeeperDistrbuteLock extends ZookeeperAbstrackLock {
    @Override
    boolean tryLock() {
        try {
            //创建临时节点成功，那么获取锁
            zkClient.createEphemeral(PATH);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    void waitLock() {
        // 使用事件监听，获取到节点被删除
        IZkDataListener iZkDataListener = new IZkDataListener() {
            //当节点发生改变
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            //当节点被删除
            @Override
            public void handleDataDeleted(String s) throws Exception {
                if (countDownLatch != null)
                    //唤醒
                    countDownLatch.countDown();
            }
        };
        //注册事件通知
        zkClient.subscribeDataChanges(PATH, iZkDataListener);
        if (zkClient.exists(PATH)) {
            // 创建信号量
            countDownLatch = new CountDownLatch(1);
            try {
                //等待
                countDownLatch.wait();
            } catch (Exception e) {

            }
        }
        //删除事件通知
        zkClient.unsubscribeDataChanges(PATH, iZkDataListener);
    }
}
