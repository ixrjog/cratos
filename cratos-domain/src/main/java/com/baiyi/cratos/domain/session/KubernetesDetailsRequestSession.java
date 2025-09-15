package com.baiyi.cratos.domain.session;

import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 15:26
 * &#064;Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KubernetesDetailsRequestSession<T extends HasSocketRequest> {

    private static final Map<String, Map<String, ? extends HasSocketRequest>> SESSION_REQUEST_MAP = new ConcurrentHashMap<>();

    public static <T extends HasSocketRequest> void putRequestMessage(String sessionId, T message) {
        if (SocketActionRequestEnum.UNSUBSCRIBE.name()
                .equalsIgnoreCase(message.getAction())) {
            if (containsBySessionId(sessionId)) {
                KubernetesDetailsRequestSession.SESSION_REQUEST_MAP.get(sessionId)
                        .remove(message.getTopic());
            }
            return;
        }
        if (containsBySessionId(sessionId)) {
            ((Map<String, T>) KubernetesDetailsRequestSession.SESSION_REQUEST_MAP.get(sessionId)).put(message.getTopic(),
                    message);
        } else {
            Map<String, T> queryMap = Maps.newHashMap();
            queryMap.put(message.getTopic(), message);
            KubernetesDetailsRequestSession.SESSION_REQUEST_MAP.put(sessionId, queryMap);
        }
    }

    public static boolean containsBySessionId(String sessionId) {
        return KubernetesDetailsRequestSession.SESSION_REQUEST_MAP.containsKey(sessionId);
    }

    public static <T extends HasSocketRequest> Map<String, T> getRequestMessageBySessionId(String sessionId) {
        return (Map<String, T>) KubernetesDetailsRequestSession.SESSION_REQUEST_MAP.get(sessionId);
    }

    public static void remove(String sessionId) {
        KubernetesDetailsRequestSession.SESSION_REQUEST_MAP.remove(sessionId);
    }

}
