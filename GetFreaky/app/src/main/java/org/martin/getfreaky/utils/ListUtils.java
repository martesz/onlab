package org.martin.getfreaky.utils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by martin on 2016. 10. 10..
 */

public class ListUtils {
    public static <T> void merge(List<T> l1, List<T> l2) {
        l1.clear();
        l1.addAll(l2);
    }
}
