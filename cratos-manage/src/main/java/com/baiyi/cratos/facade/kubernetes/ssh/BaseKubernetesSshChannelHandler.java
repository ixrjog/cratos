package com.baiyi.cratos.facade.kubernetes.ssh;

import com.baiyi.cratos.domain.channel.BaseChannelHandler;
import com.baiyi.cratos.domain.channel.factory.KubernetesSshChannelHandlerFactory;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/8 14:04
 * &#064;Version 1.0
 */
public abstract class BaseKubernetesSshChannelHandler<T extends HasSocketRequest> implements BaseChannelHandler<T> {

    @Override
    public void afterPropertiesSet() {
        KubernetesSshChannelHandlerFactory.register(this);
    }

}
