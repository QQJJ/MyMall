package com.mall.austen.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/11 9:48
 */
public class OrderUtils {

    private static long orderNumCount = 0L;

    private static String date ;

    /**
     * 订单号生成
     * @return 订单号
     */
    public static synchronized String getOrderNo(){
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        if(date == null || !date.equals(str)){
            date = str;
            orderNumCount  = 0L;
        }
        orderNumCount ++;
        long orderNo = Long.parseLong((date)) * 10000;
        orderNo += orderNumCount;
        return orderNo + "";
    }
}
