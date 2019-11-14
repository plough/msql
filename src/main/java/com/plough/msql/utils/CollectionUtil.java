package com.plough.msql.utils;

import java.util.List;

public class CollectionUtil {

    public static<T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static<T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> void printList(List<T> list) {
        for (T obj : list) {
            System.out.println(obj);
        }
    }
}
