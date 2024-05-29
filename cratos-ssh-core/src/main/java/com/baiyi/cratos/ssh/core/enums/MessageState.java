package com.baiyi.cratos.ssh.core.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:54 上午
 * @Version 1.0
 */
@Getter
public enum MessageState {

    HEARTBEAT("心跳，保持会话"),
    PLAY("播放"),
    ;

    private final String desc;

    MessageState(String desc) {
        this.desc = desc;
    }

}