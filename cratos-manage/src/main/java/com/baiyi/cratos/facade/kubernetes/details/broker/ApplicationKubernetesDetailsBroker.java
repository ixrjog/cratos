package com.baiyi.cratos.facade.kubernetes.details.broker;

import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.baiyi.cratos.domain.session.KubernetesDetailsRequestSession;
import com.baiyi.cratos.ssh.kubernetes.factory.KubernetesDetailsChannelHandlerFactory;
import com.google.common.collect.Maps;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
    private final SecurityContext context;
    private static final long SLEEP_TIME = TimeUnit.SECONDS.toMillis(8);

    public static ApplicationKubernetesDetailsBroker newBroker(String sessionId, Session session,
                                                               SecurityContext context) {
        return new ApplicationKubernetesDetailsBroker(sessionId, session, context);
    }

    public ApplicationKubernetesDetailsBroker(String sessionId, Session session, SecurityContext context) {
        this.sessionId = sessionId;
        this.session = session;
        this.context = context;
    }

    @Override
    public void run() {
        SecurityContextHolder.setContext(this.context);
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
        KubernetesDetailsRequestSession.remove(this.sessionId);
    }

    private void runTask() {
        if (!KubernetesDetailsRequestSession.containsBySessionId(this.sessionId)) {
            return;
        }
        // 深copy
        Map<String, HasSocketRequest> requestMap = Maps.newHashMap(
                KubernetesDetailsRequestSession.getRequestMessageBySessionId(this.sessionId));
        requestMap.forEach(
                (k, request) -> KubernetesDetailsChannelHandlerFactory.handleRequest(this.sessionId, null, this.session,
                        request));
    }

}
