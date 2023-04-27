package com.tuling.jvm;

/**
 * @author ming.li
 * @date 2022/12/8 20:25
 */
public class Math {
    public static final int initData = 666;
    //public static User user = new User();


    public int compute() { //一个方法对应一块栈帧内存区域
        int a = 1;
        int b = 2;
        int c = (a + b) * 10;
        return c;
    }

    public static void main(String[] args) throws InterruptedException {
        Math math = new Math();
        while (true){
            math.compute();
            Thread.sleep(10);
        }

    }

}
