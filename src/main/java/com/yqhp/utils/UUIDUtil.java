package com.yqhp.utils;

import java.util.UUID;

/**
 * Created by jiangyitao.
 */
public class UUIDUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
