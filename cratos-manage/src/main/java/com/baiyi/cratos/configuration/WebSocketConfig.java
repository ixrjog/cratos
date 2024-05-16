package com.baiyi.cratos.configuration;

import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.ErrorEnum;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.google.common.collect.Lists;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/13 下午6:04
 * &#064;Version 1.0
 */
@Configuration
@Component
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

    private static UserTokenFacade userTokenFacade;

    @Autowired
    public void setUserTokenFacade(UserTokenFacade userTokenFacade) {
        WebSocketConfig.userTokenFacade = userTokenFacade;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * WebSocket 鉴权
     * @param config
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        List<String> list = request.getHeaders()
                .get(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL);
        if (!CollectionUtils.isEmpty(list)) {
            String token = list.getFirst();
            UserToken userToken = userTokenFacade.verifyToken(token);
            String username = request.getParameterMap()
                    .get("username")
                    .getFirst();
            if (!userToken.getUsername()
                    .equals(username)) {
                throw new AuthenticationException(ErrorEnum.AUTHENTICATION_FAILED);
            }
            response.getHeaders()
                    .put(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL, Lists.newArrayList(token));
        }
        super.modifyHandshake(config, request, response);
    }

}

