package com.test;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * @author ming.li
 * @date 2023/1/4 21:57
 */
public class test {
    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        Object object = new Object();
        System.out.println(ClassLayout.parseInstance(object).toPrintable());
        synchronized (object){
            System.out.println(ClassLayout.parseInstance(object).toPrintable());
        }
    }
}
