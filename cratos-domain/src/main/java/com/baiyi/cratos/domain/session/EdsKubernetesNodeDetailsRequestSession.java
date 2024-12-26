package com.baiyi.cratos.domain.session;

import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/26 13:45
 * &#064;Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
public class EdsKubernetesNodeDetailsRequestSession<T extends HasSocketRequest> {

    private static Map<String, Map<String, ? extends HasSocketRequest>> sessionRequestMap = new HashedMap<>();

    public static <T extends HasSocketRequest> void putRequestMessage(String sessionId, T message) {
        if (SocketActionRequestEnum.UNSUBSCRIBE.name()
                .equalsIgnoreCase(message.getAction())) {
            if (containsBySessionId(sessionId)) {
                EdsKubernetesNodeDetailsRequestSession.sessionRequestMap.get(sessionId)
                        .remove(message.getTopic());
            }
            return;
        }
        if (containsBySessionId(sessionId)) {
            ((Map<String, T>) EdsKubernetesNodeDetailsRequestSession.sessionRequestMap.get(sessionId)).put(message.getTopic(),
                    message);
        } else {
            Map<String, T> queryMap = Maps.newHashMap();
            queryMap.put(message.getTopic(), message);
            EdsKubernetesNodeDetailsRequestSession.sessionRequestMap.put(sessionId, queryMap);
        }
    }

    public static boolean containsBySessionId(String sessionId) {
        return EdsKubernetesNodeDetailsRequestSession.sessionRequestMap.containsKey(sessionId);
    }

    public static <T extends HasSocketRequest> Map<String, T> getRequestMessageBySessionId(String sessionId) {
        return (Map<String, T>) EdsKubernetesNodeDetailsRequestSession.sessionRequestMap.get(sessionId);
    }

    public static void remove(String sessionId) {
        EdsKubernetesNodeDetailsRequestSession.sessionRequestMap.remove(sessionId);
    }

}