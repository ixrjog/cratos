package com.baiyi.cratos.domain.session;

import com.baiyi.cratos.domain.enums.SocketActionRequestEnum;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 15:26
 * &#064;Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
public class KubernetesDetailsRequestSession<T extends HasSocketRequest> {

    private static Map<String, Map<String, ? extends HasSocketRequest>> requestMap = new HashedMap<>();

    public static <T extends HasSocketRequest> void put(String sessionId, T message) {
        if (SocketActionRequestEnum.UNSUBSCRIBE.name()
                .equalsIgnoreCase(message.getAction())) {
            if (containsBySessionId(sessionId)) {
                KubernetesDetailsRequestSession.requestMap.get(sessionId)
                        .remove(message.getTopic());
            }
            return;
        }
        if (containsBySessionId(sessionId)) {
            ((Map<String, T>) KubernetesDetailsRequestSession.requestMap.get(sessionId)).put(message.getTopic(),
                    message);
        } else {
            Map<String, T> queryMap = Maps.newHashMap();
            queryMap.put(message.getTopic(), message);
            KubernetesDetailsRequestSession.requestMap.put(sessionId, queryMap);
        }
    }

    public static boolean containsBySessionId(String sessionId) {
        return KubernetesDetailsRequestSession.requestMap.containsKey(sessionId);
    }

    public static <T extends HasSocketRequest> Map<String, T> getBySessionId(String sessionId) {
        return (Map<String, T>) KubernetesDetailsRequestSession.requestMap.get(sessionId);
    }

    public static void remove(String sessionId) {
        KubernetesDetailsRequestSession.requestMap.remove(sessionId);
    }

}
