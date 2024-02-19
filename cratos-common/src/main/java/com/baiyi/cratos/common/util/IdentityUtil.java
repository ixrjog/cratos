package com.baiyi.cratos.common.util;

import com.baiyi.cratos.common.fun.HasRun;
import com.baiyi.cratos.common.fun.ValidRunnableFunction;

/**
 * @Author baiyi
 * @Date 2024/2/6 16:04
 * @Version 1.0
 */
public class IdentityUtil {

    private IdentityUtil() {
    }

    public static boolean hasIdentity(Integer id) {
        if (id == null) {
            return false;
        }
        return id > 0;
    }

    public static HasRun validIdentityRun(Integer id) {
        return (trueHandle) -> {
            if (hasIdentity(id)) {
                trueHandle.run();
            }
        };
    }

    public static ValidRunnableFunction tryIdentity(Integer id) {
        return (validRunnable, invalidRunnable) -> {
            if (hasIdentity(id)) {
                validRunnable.run();
            } else {
                invalidRunnable.run();
            }
        };

    }

}
