package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.configuration.socket.MyServerEndpointConfigConfig;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.guacamole.GuacamoleException;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/19 10:07
 * &#064;Version 1.0
 */
@Slf4j
@Component
@ServerEndpoint(value = "/socket/guacamole/tunnel", configurator = MyServerEndpointConfigConfig.class)
public class GuacamoleServer {


    /**
     * @param session
     * @param endpointConfig
     * @return
     * @throws GuacamoleException
     */
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
