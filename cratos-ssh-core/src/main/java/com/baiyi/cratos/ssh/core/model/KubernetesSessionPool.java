package com.baiyi.cratos.ssh.core.model;

import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/17 17:14
 * &#064;Version 1.0
 */
public class KubernetesSessionPool {

    private static final Map<String, Map<String, KubernetesSession>> KUBERNETES_SESSION_MAP = new ConcurrentHashMap<>();

    private static final Map<String, Boolean> BATCH_MAP = new ConcurrentHashMap<>();

    public static void setBatchFlag(String sessionId, Boolean isBatch) {
        BATCH_MAP.put(sessionId, isBatch);
    }

    public static Boolean getBatchFlagBySessionId(String sessionId) {
        return BATCH_MAP.get(sessionId);
    }

    public static void addSession(KubernetesSession kubernetesSession) {
        Map<String, KubernetesSession> sessionMap = KUBERNETES_SESSION_MAP.get(kubernetesSession.getSessionId());
        if (sessionMap == null) {
            sessionMap = new HashedMap<>();
            KUBERNETES_SESSION_MAP.put(kubernetesSession.getSessionId(), sessionMap);
        }
        sessionMap.put(kubernetesSession.getInstanceId(), kubernetesSession);
    }

    public static Map<String, KubernetesSession> getBySessionId(String sessionId) {
        return KUBERNETES_SESSION_MAP.get(sessionId);
    }

    public static KubernetesSession getBySessionId(String sessionId, String instanceId) {
        Map<String, KubernetesSession> sessionMap = KUBERNETES_SESSION_MAP.get(sessionId);
        if (sessionMap == null) {
            return null;
        } else {
            return sessionMap.get(instanceId);
        }
    }

    public static void removeSession(String sessionId, String instanceId) {
        Map<String, KubernetesSession> sessionMap = KUBERNETES_SESSION_MAP.get(sessionId);
        if (sessionMap != null) {
            sessionMap.remove(instanceId);
        }
    }

    /**
     * 关闭并移除会话
     *
     * @param sessionId
     * @param instanceId
     */
    public static void closeSession(String sessionId, String instanceId) {
        KubernetesSession kubernetesSession = KubernetesSessionPool.getBySessionId(sessionId, instanceId);
        if (kubernetesSession != null) {
            kubernetesSession.getWatchKubernetesTerminalOutputTask().close();
            if (kubernetesSession.getLogWatch() != null) {
                kubernetesSession.getLogWatch().close();
            }
            if (kubernetesSession.getExecWatch() != null) {
                kubernetesSession.getExecWatch().close();
            }
            kubernetesSession.setInputToChannel(null);
            kubernetesSession.setSessionOutput(null);
            kubernetesSession.setInstanceId(null);
        }
        removeSession(sessionId, instanceId);
        kubernetesSession = null;
    }
}
