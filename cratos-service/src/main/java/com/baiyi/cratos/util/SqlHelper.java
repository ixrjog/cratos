package com.baiyi.cratos.util;

import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2024/1/29 16:45
 * @Version 1.0
 */
public class SqlHelper {

    private SqlHelper() {
    }

    public static String toLike(String queryName) {
        return Joiner.on("").join("%", queryName, "%");
    }

}
