package com.baiyi.cratos.domain.enums;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 15:44
 * &#064;Version 1.0
 */
public enum SocketActionRequestEnum {

    QUERY,
    // 循环推送消息
    SUBSCRIPTION,
    UNSUBSCRIBE,
    // Kubernetes Watch Log
    WATCH,
    CLOSE

}
