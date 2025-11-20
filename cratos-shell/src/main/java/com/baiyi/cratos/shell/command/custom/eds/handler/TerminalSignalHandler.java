package com.baiyi.cratos.shell.command.custom.eds.handler;

import com.baiyi.cratos.shell.util.TerminalUtils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

/**
 * https://en.wikipedia.org/wiki/Signal_(IPC)#SIGWINCH
 * &#064;Author  baiyi
 * &#064;Date  2024/7/17 下午5:45
 * &#064;Version 1.0
 */
public class TerminalSignalHandler implements Terminal.SignalHandler {

    private final Size size = new Size();

    private final String sessionId;
    private final String instanceId;
    private final Terminal terminal;

    public TerminalSignalHandler(String sessionId, String instanceId, Terminal terminal) {
        this.sessionId = sessionId;
        this.instanceId = instanceId;
        this.terminal = terminal;
    }

    @Override
    public void handle(Terminal.Signal signal) {
        if (!terminal.getSize()
                .equals(size)) {
            size.copy(terminal.getSize());
            TerminalUtils.resize(sessionId, instanceId, size);
        }
    }

}
