package com.baiyi.cratos.controller.socket;

import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/16 14:27
 * &#064;Version 1.0
 */
@ServerEndpoint(value = "/socket/remote-gateway/tunnel", subprotocols = "guacamole")
@Component
public class RemoteDesktopGatewayServer  {

    // extends BaseGuacamoleTunnel

//    @Override
//    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {
//        Map<String, List<String>> parameterMap = session.getRequestParameterMap();
//        String token = getGuacamoleParam(parameterMap, "token");
//        SimpleLoginMessage simpleLogin = SimpleLoginMessage.builder()
//                .token(token)
//                .build();
//        String username = simpleAuthentication.hasLogin(simpleLogin);
//        return super.createTunnel(session, endpointConfig);
//    }

}
