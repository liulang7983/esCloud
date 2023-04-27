package com.set;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConcurrentHashSetRunner {

    public static void main(String[] args) {
        CopyOnWriteArraySet<Integer> copyOnWriteArraySet = new CopyOnWriteArraySet();
        Integer nextInt = new Random().nextInt();
        copyOnWriteArraySet.add(nextInt);

        System.out.println(copyOnWriteArraySet.contains(nextInt));
    }
}
