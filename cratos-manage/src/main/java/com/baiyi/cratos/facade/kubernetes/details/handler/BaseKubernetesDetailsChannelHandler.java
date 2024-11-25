package com.baiyi.cratos.facade.kubernetes.details.handler;

import com.baiyi.cratos.domain.channel.BaseChannelHandler;
import com.baiyi.cratos.domain.channel.factory.KubernetesDetailsChannelHandlerFactory;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/25 15:29
 * &#064;Version 1.0
 */
public abstract class BaseKubernetesDetailsChannelHandler implements BaseChannelHandler<ApplicationKubernetesParam.KubernetesDetailsRequest> {

    @Override
    public void afterPropertiesSet() {
        KubernetesDetailsChannelHandlerFactory.register(this);
    }

}
