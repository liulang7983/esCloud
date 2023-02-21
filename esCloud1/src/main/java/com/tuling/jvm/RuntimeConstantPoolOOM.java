package com.tuling.jvm;

import java.util.ArrayList;

/**
 * @author ming.li
 * @date 2022/12/20 21:15
 */
/**
 * jdk6：-Xms6M -Xmx6M -XX:PermSize=6M -XX:MaxPermSize=6M
 * jdk8：-Xms6M -Xmx6M -XX:MetaspaceSize=6M -XX:MaxMetaspaceSize=6M
 */
public class RuntimeConstantPoolOOM{
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 10000000; i++) {
            String str = String.valueOf(i).intern();
            list.add(str);
        }
    }
}
