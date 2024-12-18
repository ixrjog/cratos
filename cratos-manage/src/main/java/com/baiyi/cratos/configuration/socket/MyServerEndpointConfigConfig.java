package com.baiyi.cratos.configuration.socket;

import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.google.common.collect.Lists;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/13 下午6:04
 * &#064;Version 1.0
 */
@Configuration
@Component
public class MyServerEndpointConfigConfig extends ServerEndpointConfig.Configurator {

    private static UserTokenFacade userTokenFacade;

    @Autowired
    public void setUserTokenFacade(UserTokenFacade userTokenFacade) {
        setFacade(userTokenFacade);
    }

    private static void setFacade(UserTokenFacade userTokenFacade) {
        MyServerEndpointConfigConfig.userTokenFacade = userTokenFacade;
    }

    /**
     * WebSocket 鉴权
     *
     * @param config
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        final Map<String, Object> userProperties = config.getUserProperties();
        List<String> list = request.getHeaders()
                .get(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL);
        if (!CollectionUtils.isEmpty(list)) {
            String token = list.getFirst();
            try {
                UserToken userToken = userTokenFacade.verifyToken(token);
                String username = request.getParameterMap()
                        .get("username")
                        .getFirst();
                if (userToken.getUsername()
                        .equals(username)) {
                    response.getHeaders()
                            .put(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL, Lists.newArrayList(token));
                    userProperties.put("id", username);
                }
            } catch (AuthenticationException ignored) {
                // 认证失败
            }
        }
        super.modifyHandshake(config, request, response);
    }

}

