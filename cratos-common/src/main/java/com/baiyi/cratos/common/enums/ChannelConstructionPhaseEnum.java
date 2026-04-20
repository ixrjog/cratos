package com.baiyi.cratos.common.enums;

import com.baiyi.cratos.common.exception.BusinessException;
import com.google.common.base.Joiner;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 16:42
 * &#064;Version 1.0
 */
public enum ChannelConstructionPhaseEnum {

    PLANNING,
    BUILDING,
    TESTING,
    COMPLETED;

    public static void verifyValueOf(String constructionPhase) {
        try {
            ChannelConstructionPhaseEnum.valueOf(constructionPhase);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Unsupported constructionPhase! please enter: {}", Joiner.on(",")
                    .join(ChannelConstructionPhaseEnum.values()));
        }
    }

}