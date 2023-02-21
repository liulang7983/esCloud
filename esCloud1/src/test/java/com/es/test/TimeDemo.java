package com.es.test;

import org.junit.jupiter.api.Test;

/**
 * @author ming.li
 * @date 2022/12/9 15:28
 */
public class TimeDemo {
    @Test
    public void test1(){
        String start="01:59:11";
        String end="02:00:00";
        System.out.println(start.compareTo(end));
    }
}
