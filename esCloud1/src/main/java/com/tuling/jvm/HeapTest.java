package com.tuling.jvm;

import java.util.ArrayList;

/**
 * @author ming.li
 * @date 2022/12/19 22:21
 */
public class HeapTest {
    byte[] a = new byte[1024 * 100];  //100KB

    public static void main(String[] args) throws InterruptedException {
        ArrayList<HeapTest> heapTests = new ArrayList<>();
        while (true) {
            heapTests.add(new HeapTest());
            Thread.sleep(10);
        }
    }
}
