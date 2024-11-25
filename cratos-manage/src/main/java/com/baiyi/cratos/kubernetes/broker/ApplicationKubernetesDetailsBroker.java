package com.baiyi.cratos.kubernetes.broker;

import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.baiyi.cratos.domain.session.KubernetesDetailsRequestSession;
import com.baiyi.cratos.kubernetes.KubernetesDetailsChannelHandlerFactory;
import com.google.common.collect.Maps;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 16:34
 * &#064;Version 1.0
 */
@Slf4j
public class ApplicationKubernetesDetailsBroker implements Runnable {

    private final String sessionId;
    private final Session session;

    public ApplicationKubernetesDetailsBroker(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public void run() {
        if (!this.session.isOpen()) {
            KubernetesDetailsRequestSession.remove(this.sessionId);
            return;
        }
        try {
            if (KubernetesDetailsRequestSession.containsBySessionId(this.sessionId)) {
                // 深copy
                Map<String, HasSocketRequest> queryMap = Maps.newHashMap(
                        KubernetesDetailsRequestSession.getRequestMessageBySessionId(this.sessionId));
                queryMap.forEach((k, request) -> KubernetesDetailsChannelHandlerFactory.handleRequest(this.sessionId,
                        this.session, request));
            }
            TimeUnit.MILLISECONDS.sleep(150L);
        } catch (InterruptedException e) {
            Thread.currentThread()
                    .interrupt();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

}
