package com.baiyi.cratos.ssh.crystal.factory;

import com.baiyi.cratos.ssh.crystal.SshCrystalMessageHandler;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/24 17:28
 * &#064;Version 1.0
 */
@Slf4j
public class SshCrystalMessageHandlerFactory {

    private static final Map<String, SshCrystalMessageHandler> CONTEXT = new ConcurrentHashMap<>();

    public static SshCrystalMessageHandler getByState(String state) {
        SshCrystalMessageHandler handler = CONTEXT.get(state);
        return handler != null ? handler : CONTEXT.get(MessageState.UNKNOWN.name());
    }

    public static void register(SshCrystalMessageHandler bean) {
        log.debug("SshCrystalMessageHandlerFactory Registered: state={}", bean.getState());
        CONTEXT.put(bean.getState(), bean);
    }

}
