package com.wsq.common;

import com.wsq.common.base.tools.JsonUtil;

import java.util.Map;
import java.util.TreeMap;

public class TestMain {



    public static void main(String[] args) {
        TreeMap<String, String> map = new TreeMap();
        map.put("a3c", "s");
        map.put("a2c", "s");
        map.put("1111", "s");
        map.put("1", "s");
        System.out.println(JsonUtil.toJson(map));
//        for (Map.Entry entry :map.entrySet()){
//            System.out.println("result:" + entry.getKey());
//        }

        String a = "JTdCJTIyMSUyMiUzQSUyMnMlMjIlMkMlMjIxMTExJTIyJTNBJTIycyUyMiUyQyUyMmElMjIlM0ElMjJzJTIyJTJDJTIyYiUyMiUzQSUyMnMlMjIlN0Q=";
        String b = "JTdCJTIyMTExMSUyMiUzQSUyMnMlMjIlMkMlMjIxJTIyJTNBJTIycyUyMiUyQyUyMmElMjIlM0ElMjJzJTIyJTJDJTIyYiUyMiUzQSUyMnMlMjIlN0Q=";
//        System.out.println(a.equals(b));
    }

}
