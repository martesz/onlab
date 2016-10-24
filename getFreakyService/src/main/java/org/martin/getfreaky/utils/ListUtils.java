/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.utils;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author martin
 */
public class ListUtils {

    // Merges the content of the lists
    public static <T> void merge(List<T> existing, List<T> uploaded) {
        Iterator<T> it = existing.iterator();
        while (it.hasNext()) {
            if (!uploaded.contains(it.next())) {
                it.remove();
            }
        }
        for (T t : uploaded) {
            if (!existing.contains(t)) {
                existing.add(t);
            }
        }
    }
}
