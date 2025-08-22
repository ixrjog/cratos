package com.baiyi.cratos.ssh.core.model;

import com.baiyi.cratos.common.util.SshIdUtils;
import org.apache.sshd.common.io.IoSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午3:07
 * &#064;Version 1.0
 */
public class SshSessionIdMapper {

    private static final ConcurrentHashMap<Long, String> MAPPER = new ConcurrentHashMap<>();

    public static void put(IoSession ioSession) {
        MAPPER.put(ioSession.getId(), SshIdUtils.generateID());
    }

    public static String getSessionId(IoSession ioSession) {
        return MAPPER.get(ioSession.getId());
    }

    public static void remove(IoSession ioSession) {
        MAPPER.remove(ioSession.getId());
    }

    public static void remove(Long ioSessionId) {
        MAPPER.remove(ioSessionId);
    }

}
