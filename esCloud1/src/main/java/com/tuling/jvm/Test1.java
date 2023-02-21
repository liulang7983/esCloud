package com.tuling.jvm;

/**
 * @author ming.li
 * @date 2022/12/20 21:58
 */
public class Test1 {
    public static void main(String[] args) {
        String s1=new StringBuilder("abc").toString();
        String s2=s1.intern();
        System.out.println(s1==s2);

        String s3=new StringBuilder("abc").append("d").toString();
        String s4=s3.intern();
        System.out.println(s3==s4);

        String s5=new String("abc");
        String s6=s5.intern();
        System.out.println(s5==s6);

        String s7=new String("abc")+new String("de");
        String s8=s7.intern();
        System.out.println(s7==s8);
    }
}
