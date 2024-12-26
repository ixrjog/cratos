package com.baiyi.cratos.facade.kubernetes.node.handler;

import com.baiyi.cratos.domain.channel.BaseChannelHandler;
import com.baiyi.cratos.domain.channel.factory.EdsKubernetesNodeDetailsChannelHandlerFactory;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/26 14:05
 * &#064;Version 1.0
 */
public abstract class BaseEdsKubernetesNodeDetailsChannelHandler<T extends HasSocketRequest> implements BaseChannelHandler<T> {

    @Override
    public void afterPropertiesSet() {
        EdsKubernetesNodeDetailsChannelHandlerFactory.register(this);
    }

}