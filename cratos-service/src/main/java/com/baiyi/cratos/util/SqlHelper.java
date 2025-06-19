package com.baiyi.cratos.util;

import com.google.common.base.Joiner;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/29 16:45
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class SqlHelper {

    public static String likeOf(String queryName) {
        return Joiner.on("")
                .join("%", queryName, "%");
    }

}
