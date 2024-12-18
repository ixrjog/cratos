package com.baiyi.cratos.domain.channel;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 10:01
 * &#064;Version 1.0
 */
public interface HasTopic {

    String HEARTBEAT = "HEARTBEAT";
    String APPLICATION_KUBERNETES_DETAILS = "APPLICATION_KUBERNETES_DETAILS";
    String APPLICATION_KUBERNETES_WATCH_LOG = "APPLICATION_KUBERNETES_WATCH_LOG";
    String PLAY_SSH_SESSION_AUDIT = "PLAY_SSH_SESSION_AUDIT";

    String ERROR =  "ERROR";

    String getTopic();
}
