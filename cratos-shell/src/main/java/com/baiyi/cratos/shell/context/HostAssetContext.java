package com.baiyi.cratos.shell.context;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午5:36
 * &#064;Version 1.0
 */
public class HostAssetContext {

    private static final ThreadLocal<Map<Integer, String>> CONTEXT = new ThreadLocal<>();

    public static void setContext(Map<Integer, String> param) {
        CONTEXT.set(param);
    }

    public static Map<Integer, String> getContext() {
        return CONTEXT.get();
    }

    public static void remove() {
        CONTEXT.remove();
    }

}
