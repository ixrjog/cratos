package com.baiyi.cratos.facade.kubernetes.details.broker;

import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.baiyi.cratos.domain.session.KubernetesDetailsRequestSession;
import com.baiyi.cratos.domain.channel.factory.KubernetesDetailsChannelHandlerFactory;
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
        while (this.session.isOpen()) {
            try {
                runTask();
                TimeUnit.MILLISECONDS.sleep(3000L);
            } catch (InterruptedException e) {
                Thread.currentThread()
                        .interrupt();
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        KubernetesDetailsRequestSession.remove(this.sessionId);
    }

    private void runTask() {
        if (!KubernetesDetailsRequestSession.containsBySessionId(this.sessionId)) {
            return;
        }
        // 深copy
        Map<String, HasSocketRequest> queryMap = Maps.newHashMap(
                KubernetesDetailsRequestSession.getRequestMessageBySessionId(this.sessionId));
        queryMap.forEach(
                (k, request) -> KubernetesDetailsChannelHandlerFactory.handleRequest(this.sessionId, this.session,
                        request));
    }

}
