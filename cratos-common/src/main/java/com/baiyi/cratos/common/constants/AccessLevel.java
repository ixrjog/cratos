package com.baiyi.cratos.common.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2024/1/17 15:18
 * @Version 1.0
 */
@Getter
public enum AccessLevel {

    FOUNDER(100),
    ADMIN(90),
    OPS(50),
    BASE(1),
    CUSTOM(0);

    private final int level;

    AccessLevel(int level) {
        this.level = level;
    }

}
