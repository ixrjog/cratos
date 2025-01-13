package com.baiyi.cratos.facade.kubernetes.node.broker;

import com.baiyi.cratos.ssh.kubernetes.factory.KubernetesDetailsChannelHandlerFactory;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.baiyi.cratos.domain.session.EdsKubernetesNodeDetailsRequestSession;
import com.google.common.collect.Maps;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/26 13:52
 * &#064;Version 1.0
 */
@Slf4j
public class EdsKubernetesNodeDetailsBroker implements Runnable {

    private final String sessionId;
    private final Session session;

    private static final long SLEEP_TIME = TimeUnit.SECONDS.toMillis(8);

    public static EdsKubernetesNodeDetailsBroker newBroker(String sessionId, Session session) {
        return new EdsKubernetesNodeDetailsBroker(sessionId, session);
    }

    public EdsKubernetesNodeDetailsBroker(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    @Override
    public void run() {
        while (this.session.isOpen()) {
            try {
                runTask();
                TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                Thread.currentThread()
                        .interrupt();
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        close();
    }

    private void close() {
        EdsKubernetesNodeDetailsRequestSession.remove(this.sessionId);
    }

    private void runTask() {
        if (!EdsKubernetesNodeDetailsRequestSession.containsBySessionId(this.sessionId)) {
            return;
        }
        // 深copy
        Map<String, HasSocketRequest> requestMap = Maps.newHashMap(
                EdsKubernetesNodeDetailsRequestSession.getRequestMessageBySessionId(this.sessionId));
        requestMap.forEach(
                (k, request) -> KubernetesDetailsChannelHandlerFactory.handleRequest(this.sessionId, this.session,
                        request));
    }

}

