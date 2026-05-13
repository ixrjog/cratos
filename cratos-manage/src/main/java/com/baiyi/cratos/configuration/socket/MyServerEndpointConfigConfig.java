package com.baiyi.cratos.configuration.socket;

import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.RequestSignUtil;
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
import org.springframework.util.StringUtils;

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
     * WebSocket 鉴权（兼容 token 模式和签名模式）
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        final Map<String, Object> userProperties = config.getUserProperties();
        Map<String, List<String>> params = request.getParameterMap();

        // 优先使用签名模式
        String jti = getParam(params, "jti");
        String timestamp = getParam(params, "t");
        String sign = getParam(params, "sign");

        if (StringUtils.hasText(jti) && StringUtils.hasText(timestamp) && StringUtils.hasText(sign)) {
            // 签名模式
            if (!RequestSignUtil.isTimestampValid(timestamp)) {
                return;
            }
            UserToken userToken = userTokenFacade.getByJti(jti);
            if (userToken == null || !userToken.getValid()) {
                return;
            }
            String signData = jti + timestamp;
            if (!RequestSignUtil.verify(signData, userToken.getToken(), sign)) {
                return;
            }
            userProperties.put("id", userToken.getUsername());
            // 设置 protocol 响应头（必须响应一个 protocol 否则浏览器会断开）
            List<String> protocols = request.getHeaders().get(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL);
            if (!CollectionUtils.isEmpty(protocols)) {
                response.getHeaders().put(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL, Lists.newArrayList(protocols.getFirst()));
            }
        } else {
            // 兼容旧 token 模式
            List<String> list = request.getHeaders().get(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL);
            if (!CollectionUtils.isEmpty(list)) {
                String token = list.getFirst();
                try {
                    UserToken userToken = userTokenFacade.verifyToken(token);
                    String username = getParam(params, "username");
                    if (username == null) {
                        // 从路径中获取
                        username = userToken.getUsername();
                    }
                    if (userToken.getUsername().equals(username)) {
                        response.getHeaders().put(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL, Lists.newArrayList(token));
                        userProperties.put("id", username);
                    }
                } catch (AuthenticationException ignored) {
                }
            }
        }
        super.modifyHandshake(config, request, response);
    }

    private String getParam(Map<String, List<String>> params, String key) {
        List<String> values = params.get(key);
        return (values != null && !values.isEmpty()) ? values.getFirst() : null;
    }

}

