package com.baiyi.cratos.domain.channel;

import org.springframework.aop.support.AopUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 10:01
 * &#064;Version 1.0
 */
public interface HasTopic {

    String HEARTBEAT = "HEARTBEAT";
    String APPLICATION_KUBERNETES_DETAILS = "APPLICATION_KUBERNETES_DETAILS";
    String APPLICATION_KUBERNETES_POD_WATCH_LOG = "APPLICATION_KUBERNETES_POD_WATCH_LOG";
    String APPLICATION_KUBERNETES_POD_EXEC = "APPLICATION_KUBERNETES_POD_EXEC";

    String PLAY_SSH_SESSION_AUDIT = "PLAY_SSH_SESSION_AUDIT";
    String EDS_KUBERNETES_NODE_DETAILS = "EDS_KUBERNETES_NODE_DETAILS";

    String ERROR =  "ERROR";

    default String getTopic() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(com.baiyi.cratos.domain.annotation.TopicName.class)
                .nameOf();
    }

}
