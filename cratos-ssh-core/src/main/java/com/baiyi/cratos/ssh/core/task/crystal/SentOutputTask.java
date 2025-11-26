package com.baiyi.cratos.ssh.core.task.crystal;

import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.util.SessionOutputUtils;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/24 17:49
 * &#064;Version 1.0
 */
@Slf4j
public class SentOutputTask implements Runnable {

    private final Session session;
    private final String sessionId;

    public SentOutputTask(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    public static SentOutputTask newTask(String sessionId, Session session) {
        return new SentOutputTask(sessionId, session);
    }

    @Override
    public void run() {
        try {
            while (session.isOpen()) {
                List<SessionOutput> outputList = SessionOutputUtils.getOutput(sessionId);
                if (!CollectionUtils.isEmpty(outputList)) {
                    String jsonStr = JSONUtils.writeValueAsString(outputList);
                    session.getBasicRemote()
                            .sendText(jsonStr);
                }
                TimeUnit.MILLISECONDS.sleep(25L);
            }
        } catch (InterruptedException interruptedException) {
            Thread.currentThread()
                    .interrupt();
        } catch (IOException ioException) {
            log.debug(ioException.getMessage());
        }
    }

}