package com.baiyi.cratos.ssh.core.player;

import com.baiyi.cratos.domain.channel.BaseChannelHandler;
import com.baiyi.cratos.domain.channel.factory.SshAuditChannelHandlerFactory;
import com.baiyi.cratos.domain.param.socket.audit.SshSessionAuditParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/25 16:05
 * &#064;Version 1.0
 */
public abstract class BaseSshAuditChannelHandler implements BaseChannelHandler<SshSessionAuditParam.AuditRequest> {

    @Override
    public void afterPropertiesSet() {
        SshAuditChannelHandlerFactory.register(this);
    }

}
