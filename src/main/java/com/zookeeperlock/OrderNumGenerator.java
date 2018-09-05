package com.zookeeperlock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mr.PanYang on 2018/8/21.
 * <p>
 * 生成订单号，不能重复
 * 规则：时间戳 + 业务 ID
 */
public class OrderNumGenerator {

    private static int count = 0;

    // 获取订单号
    public String getNumber() {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return simp.format(new Date()) + "-" + ++count;
    }
}
