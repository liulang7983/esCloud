package com.test;

/**
 * @author ming.li
 * @date 2023/1/2 18:55
 */
public class AtomicTest {
    private static int counter=0;
    private static Object o=new Object();
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    synchronized (o){
                        counter++;
                    }
                }
            });
            thread.start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(counter);
    }
}
