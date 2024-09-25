package com.baiyi.cratos.ssh.crystal.factory;

import com.baiyi.cratos.ssh.core.SshCrystalMessageHandler;
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

    static Map<String, SshCrystalMessageHandler> context = new ConcurrentHashMap<>();

    public static SshCrystalMessageHandler getByState(String state) {
        return context.get(state);
    }

    public static void register(SshCrystalMessageHandler bean) {
        log.debug("SshCrystalMessageHandlerFactory  Registered: state={}", bean.getState());
        context.put(bean.getState(), bean);
    }

}
