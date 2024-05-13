package com.baiyi.cratos.configuration;

import com.google.common.collect.Lists;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/13 下午6:04
 * &#064;Version 1.0
 */
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        List<String> list = request.getHeaders()
                .get(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL);
        if (!CollectionUtils.isEmpty(list)) {
            String s = list.get(0);
            String[] split = s.split(",");
            List<String> resList = Lists.newArrayList(split[0]);
            response.getHeaders()
                    .put(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL, resList);
        }
        super.modifyHandshake(config, request, response);
    }

}

