package com.baiyi.cratos.ssh.core.handler;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.core.model.JSchSession;
import com.baiyi.cratos.ssh.core.model.JSchSessionHolder;
import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.util.ChannelShellUtil;
import com.baiyi.cratos.ssh.core.util.SessionConfigUtil;
import com.baiyi.cratos.ssh.core.watch.ssh.WatchSshServerOutputTask;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.SshException;
import org.jline.terminal.Size;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午5:27
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class RemoteInvokeHandler {

    private static final int SSH_PORT = 22;

    private static final String appId = UUID.randomUUID()
            .toString();

    public static void openSSHServer(String sessionId, HostSystem hostSystem, OutputStream out) throws SshException {
        JSch jsch = new JSch();
        hostSystem.setStatusCd(HostSystem.SUCCESS_STATUS);
        try {
            if (hostSystem.getCredential() == null) {
                return;
            }
            Session session = jsch.getSession(hostSystem.getLoginUsername(), hostSystem.getHost(),
                    hostSystem.getPort() == null ? SSH_PORT : hostSystem.getPort());
            // 设置凭据
            setSshCredential(hostSystem, jsch, session);
            // 默认设置
            SessionConfigUtil.setDefault(session);
            ChannelShell channel = (ChannelShell) session.openChannel("shell");

            ChannelShellUtil.setDefault(channel);
            // 初始化PtySize
            setChannelPtySize(channel, hostSystem.getTerminalSize());
            // new session output
            SessionOutput sessionOutput = new SessionOutput(sessionId, hostSystem);
            // 启动线程处理会话
            Runnable run = new WatchSshServerOutputTask(sessionOutput, channel.getInputStream(), out,
                    hostSystem.getAuditPath());
            // JDK21 VirtualThreads
            Thread.ofVirtual()
                    .start(run);

            OutputStream inputToChannel = channel.getOutputStream();

            JSchSession jSchSession = JSchSession.builder()
                    .sessionId(sessionId)
                    .instanceId(hostSystem.getInstanceId())
                    .commander(new PrintStream(inputToChannel, true, StandardCharsets.UTF_8))
                    .inputToChannel(inputToChannel)
                    .channel(channel)
                    .hostSystem(hostSystem)
                    .build();
            jSchSession.setSessionOutput(sessionOutput);
            JSchSessionHolder.addSession(jSchSession);
            channel.connect();
        } catch (Exception e) {
            if (e.getMessage()
                    .toLowerCase()
                    .contains("userauth fail")) {
                hostSystem.setStatusCd(HostSystem.PUBLIC_KEY_FAIL_STATUS);
            } else if (e.getMessage()
                    .toLowerCase()
                    .contains("auth fail") || e.getMessage()
                    .toLowerCase()
                    .contains("auth cancel")) {
                hostSystem.setStatusCd(HostSystem.AUTH_FAIL_STATUS);
            } else if (e.getMessage()
                    .toLowerCase()
                    .contains("unknownhostexception")) {
                hostSystem.setErrorMsg("DNS Lookup Failed");
                hostSystem.setStatusCd(HostSystem.HOST_FAIL_STATUS);
            } else {
                hostSystem.setStatusCd(HostSystem.GENERIC_FAIL_STATUS);
            }
            throw new SshException(e.toString());
        }
    }

    /**
     * 按凭据类型设置密钥
     *
     * @param hostSystem
     * @param jsch
     * @param session
     * @throws JSchException
     */
    private static void setSshCredential(HostSystem hostSystem, JSch jsch, Session session) throws JSchException {
        Credential credential = hostSystem.getCredential();
        CredentialTypeEnum credentialTypeEnum = CredentialTypeEnum.valueOf(credential.getCredentialType());
        boolean hasPassphrase = StringUtils.hasText(credential.getPassphrase());
        switch (credentialTypeEnum) {
            case CredentialTypeEnum.USERNAME_WITH_PASSWORD -> session.setPassword(credential.getCredential());
            case CredentialTypeEnum.SSH_USERNAME_WITH_PRIVATE_KEY, CredentialTypeEnum.SSH_USERNAME_WITH_KEY_PAIR -> {
                byte[] prvkey = credential.getCredential()
                        .trim()
                        .getBytes();
                byte[] pubkey = StringUtils.hasText(credential.getCredential2()) ? credential.getCredential2()
                        .getBytes() : null;
                byte[] passphrase = hasPassphrase ? credential.getPassphrase()
                        .getBytes() : null;
                jsch.addIdentity(appId, prvkey, pubkey, passphrase);
            }
            default -> throw new IllegalStateException("Unexpected value: " + credentialTypeEnum.name());
        }

    }

//    public static void setChannelPtySize(ChannelShell channel, ServerMessage.BaseMessage message) {
//        if (channel == null || channel.isClosed()) {
//            return;
//        }
//        channel.setPtySize(message.getCols(), message.getRows(), message.getWidth(), message.getHeight());
//    }

    public static void setChannelPtySize(ChannelShell channel, Size size) {
        if (channel == null || channel.isClosed()) {
            return;
        }
        channel.setPtySize(size.getColumns(), size.getRows(), size.getColumns() * 7,
                (int) Math.floor(size.getRows() / 14.4166));
    }

}
