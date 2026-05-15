/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sofya.java.uphone.apiRest.paymentPointBackOffice.caching;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author CBARRAGAN
 */
public class lru {

    static Deque<String> q = new LinkedList<>();
    static Map<String, Cache> map = new HashMap<>();
    int CACHE_CAPACITY = 10;

    public String getElementFromCache(String key) {
        if (map.containsKey(key)) {
            Cache current = map.get(key);
            q.remove(current.key);
            q.addFirst(current.key);
            return current.value;
        }
        return "Not exist";
    }

    public void putElementInCache(String key, String value) {
        if (map.containsKey(key)) {
            Cache curr = map.get(key);
            q.remove(curr.key);
        } else {
            if (q.size() == CACHE_CAPACITY) {
                String temp = q.removeLast();
                map.remove(temp);
            }
        }
        Cache newItem = new Cache(key, value);
        q.addFirst(newItem.key);
        map.put(key, newItem);
    }

    public void deleteElemetCache(String key){
        if (map.containsKey(key)) {
            map.remove(key);
            q.remove(key);
        }
    }
}
