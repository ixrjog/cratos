package com.baiyi.cratos.ssh.core.player;

import com.baiyi.cratos.ssh.core.message.output.OutputMessage;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2021/7/23 3:29 下午
 * @Version 1.0
 */
@Slf4j
public class SshAuditOutputTask implements Runnable {

    private final Session session;
    private final String instanceId;
    private final String auditPath;

    public SshAuditOutputTask(Session session, String instanceId, String auditPath) {
        this.session = session;
        this.instanceId = instanceId;
        this.auditPath = auditPath;
    }

    @Override
    public void run() {
        try (LineNumberReader reader = new LineNumberReader(new FileReader(auditPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    sendOutput(line + "\n");
                }
                TimeUnit.MILLISECONDS.sleep(25L);
            }
        } catch (IOException ioException) {
            try {
                sendError(ioException.getMessage());
            } catch (IOException ignored) {
            }
        } catch (InterruptedException e) {
            Thread.currentThread()
                    .interrupt();
        }
    }

    private void sendOutput(String line) throws IOException {
        OutputMessage om = OutputMessage.builder()
                .instanceId(instanceId)
                .output(line)
                .build();
        send(om);
    }

    private void sendError(String error) throws IOException {
        OutputMessage om = OutputMessage.builder()
                .instanceId(instanceId)
                .error(error)
                .code(1)
                .build();
        send(om);
    }

    private void send(OutputMessage om) throws IOException {
        session.getBasicRemote()
                .sendText(om.toString());
    }

}