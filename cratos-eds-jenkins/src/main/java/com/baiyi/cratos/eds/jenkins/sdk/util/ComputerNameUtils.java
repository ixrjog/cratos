package com.baiyi.cratos.eds.jenkins.sdk.util;

import com.baiyi.cratos.eds.jenkins.sdk.client.util.EncodingUtils;

/**
 * @Author baiyi
 * @Date 2022/4/6 14:49
 * @Version 1.0
 */
public class ComputerNameUtils {

    private static final String BUILT_IN_NODE_NAME = "built-in node";

    public static String toName(String displayName) {
        return BUILT_IN_NODE_NAME.equalsIgnoreCase(displayName) ? "(built-in)" : EncodingUtils.encode(displayName);
    }

}