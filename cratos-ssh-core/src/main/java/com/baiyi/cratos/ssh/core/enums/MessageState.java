package com.baiyi.cratos.ssh.core.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:54 上午
 * @Version 1.0
 */
@Getter
public enum MessageState {

    HEARTBEAT("会话保持"),
    PLAY("播放"),
    LOGIN("登录服务器"),
    COMMAND("交互命令"),
    DUPLICATE("复制会话"),
    DUPLICATE_BY_IP("复制会话"),
    RESIZE("改变窗体"),
    CLOSE("关闭会话(登出所有服务器)"),
    LOGOUT("登出服务器"),
    BATCH_COMMAND("批量命令"),
    UNKNOWN("未知的");

    private final String desc;

    MessageState(String desc) {
        this.desc = desc;
    }

}