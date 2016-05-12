package org.martin.getfreaky.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by martin on 2016. 05. 09..
 */
public class Sequence {
    private static AtomicInteger counter = new AtomicInteger();

    public static int nextValue() {
        return counter.getAndIncrement();
    }

    public static void setStart(int i) {
        counter = new AtomicInteger(i);
    }

    public static int getCurrentValue() {
        return counter.get();
    }
}
