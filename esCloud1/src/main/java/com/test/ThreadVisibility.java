package com.test;

/**
 * @author ming.li
 * @date 2023/1/2 17:12
 */
public class ThreadVisibility {
    private  static boolean initFlag=false;
    private static int counter=0;
    public static void refresh(){
        System.out.println("开始");
        initFlag=true;
        System.out.println("结束");
    }

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            while (!initFlag){
                System.out.println("打印");
            }
            System.out.println("当前线程发现了initFlag状态改变");
        },"ThreadA");
        threadA.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread threadB = new Thread(() -> {
            refresh();
        }, "ThreadB");
        threadB.start();
    }
}
