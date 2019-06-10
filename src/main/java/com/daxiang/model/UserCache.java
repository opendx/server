package com.daxiang.model;

import com.daxiang.mbg.po.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiangyitao.
 */
public class UserCache {
    private static final Map<Integer, User> cache = new ConcurrentHashMap();

    public static void set(Map<Integer, User> userMap) {
        cache.putAll(userMap);
    }

    public static User getById(Integer uid) {
        return cache.get(uid);
    }

    public static void add(Integer uid, User user) {
        cache.put(uid, user);
    }

    public static String getNickNameById(Integer uid) {
        if (uid == null) {
            return null;
        }
        User user = getById(uid);
        return user == null ? null : user.getNickName();
    }
}
