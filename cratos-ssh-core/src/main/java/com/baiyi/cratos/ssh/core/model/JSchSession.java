package com.baiyi.cratos.ssh.core.model;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;
import java.io.PrintStream;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JSchSession {

    private String termSessionId;

    private String sessionId;
    /**
     * 服务器唯一id
     * 会话复制后 id#uuid
     */
    private String instanceId;

    private PrintStream commander;
    private OutputStream inputToChannel;
    private Channel channel;
    private HostSystem hostSystem;
    private SessionOutput sessionOutput;

    private Session proxySession;


    public void destroy() {
        if (this.channel != null) {
            channel.disconnect();
            channel = null;
        }
        if (proxySession != null) {
            proxySession.disconnect();
            proxySession = null;
        }
        this.commander = null;
        this.inputToChannel = null;
        this.termSessionId = null;
        this.sessionOutput = null;
        this.instanceId = null;
        this.hostSystem = null;
    }

}