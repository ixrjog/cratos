package com.baiyi.cratos.ssh.core.model;

import com.baiyi.cratos.domain.param.HasTerminalSize;
import com.baiyi.cratos.ssh.core.watch.kubernetes.WatchKubernetesTerminalOutputTask;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/17 17:07
 * &#064;Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KubernetesSession {

    private String sessionId;
    /**
     * 服务器唯一id
     * 会话复制后 id#uuid
     */
    private String instanceId;
    private PrintStream commander;
    private LogWatch logWatch;
    private ExecWatch execWatch;
    private WatchKubernetesTerminalOutputTask watchKubernetesTerminalOutputTask;
    private OutputStream inputToChannel;
    private static SessionOutput sessionOutput;

    public void setSessionOutput(SessionOutput sessionOutput) {
        KubernetesSession.sessionOutput = sessionOutput;
    }

    public SessionOutput getSessionOutput() {
        return KubernetesSession.sessionOutput;
    }

    public void resize(HasTerminalSize terminalSize) {
        if (this.execWatch == null) {
            return;
        }
        execWatch.resize(terminalSize.getTerminalCols(), terminalSize.getTerminalRows());
    }

}
