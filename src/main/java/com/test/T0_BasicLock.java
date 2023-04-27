package com.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author ming.li
 * @date 2023/1/4 22:36
 */
@Slf4j
public class T0_BasicLock {
    public static void main(String[] args) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Object o = new Object();
        log.info(ClassLayout.parseInstance(o).toPrintable());

        new Thread(()->{
            synchronized (o){
                log.info(ClassLayout.parseInstance(o).toPrintable());
            }
        }).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info(ClassLayout.parseInstance(o).toPrintable());
        new Thread(()->{
            synchronized (o){
                log.info(ClassLayout.parseInstance(o).toPrintable());
            }
        }).start();
    }
}

