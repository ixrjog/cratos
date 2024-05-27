package com.baiyi.cratos.ssh.core.model;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class JSchSessionHolder {

    private static Map<String, ConcurrentHashMap<String, JSchSession>> jSchSessionMap = new ConcurrentHashMap<>();

    private static Map<String, Boolean> batchMap = new ConcurrentHashMap<>();

    public static void setBatch(String sessionId, Boolean isBatch) {
        batchMap.put(sessionId, isBatch);
    }

    public static Boolean getBatchFlagBySessionId(String sessionId) {
        return batchMap.get(sessionId);
    }

    public static void addSession(JSchSession jSchSession) {
        ConcurrentHashMap<String, JSchSession> sessionMap = jSchSessionMap.computeIfAbsent(jSchSession.getSessionId(), k -> new ConcurrentHashMap<>());
        sessionMap.put(jSchSession.getInstanceId(), jSchSession);
    }

    public static Map<String, JSchSession> getSession(String sessionId) {
        return jSchSessionMap.get(sessionId);
    }

    public static JSchSession getSession(String sessionId, String instanceId) {
        Map<String, JSchSession> sessionMap = jSchSessionMap.get(sessionId);
        if (sessionMap == null) {
            return null;
        } else {
            return sessionMap.get(instanceId);
        }
    }

    public static void removeSession(String sessionId, String instanceId) {
        Map<String, JSchSession> sessionMap = jSchSessionMap.get(sessionId);
        if (sessionMap != null)
            sessionMap.remove(instanceId);
    }

    /**
     * 关闭并移除会话
     * @param sessionId
     * @param instanceId
     */
    public static void closeSession(String sessionId, String instanceId) {
        JSchSession jSchSession = JSchSessionHolder.getSession(sessionId, instanceId);
        if (jSchSession != null) {
            jSchSession.preDestruction();
            jSchSession = null;
        }
        removeSession(sessionId, instanceId);
    }

}