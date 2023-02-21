package com.tuling.jvm;

/**
 * @author ming.li
 * @date 2022/12/20 21:34
 */
public class StringTest {
    public static void main(String[] args) {
        String s1 = new String("he") + new String("llo");
        String s2=s1.intern();
        System.out.println(s1==s2);
    }
}
