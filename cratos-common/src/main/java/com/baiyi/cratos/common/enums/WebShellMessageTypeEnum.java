package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:54 上午
 * @Version 1.0
 */
@Getter
public enum WebShellMessageTypeEnum {

    INIT,
    HEARTBEAT,
    INPUT,
    INPUT_ALL,
    DUPLICATE_SESSION,
    RESIZE,
    CLOSE,
    LOGOUT,
    PLAY

}