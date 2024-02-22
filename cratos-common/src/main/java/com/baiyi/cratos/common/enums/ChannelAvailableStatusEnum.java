package com.baiyi.cratos.common.enums;

import com.baiyi.cratos.common.exception.BusinessException;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2024/2/21 15:39
 * @Version 1.0
 */
public enum ChannelAvailableStatusEnum {

    HA,
    UNSTABLE,
    DOWN;

    public static void verifyValueOf(String availableStatus) {
        try {
            ChannelAvailableStatusEnum.valueOf(availableStatus);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Unsupported availableStatus! please enter: {}", Joiner.on(",")
                    .join(ChannelAvailableStatusEnum.values()));
        }
    }

}
