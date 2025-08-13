package com.baiyi.cratos.ssh.core.handler;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.core.model.JSchSession;
import com.baiyi.cratos.ssh.core.model.JSchSessionHolder;
import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.util.ChannelShellUtils;
import com.baiyi.cratos.ssh.core.util.SessionConfigUtils;
import com.baiyi.cratos.ssh.core.watch.crystal.WatchSshCrystalTerminalOutputTask;
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
 * 优化后的SSH远程调用处理器
 * <p>
 * 重要说明：
 * 1. 代理连接使用独立的JSch实例，避免密钥冲突
 * 2. 代理Session存储在JSchSession中，通过destroy()方法统一清理
 * 3. JSchSession的destroy()方法会自动清理代理资源和目标资源
 *
 * @Author baiyi
 * @Date 2024/5/23 下午5:27
 * @Version 2.1
 */
@Slf4j
@Component
public class RemoteInvokeHandler {

    //private static final int SSH_PORT = 22;
    private static final String LOCALHOST = "127.0.0.1";
    private static final String appId = UUID.randomUUID()
            .toString();

    public static void openSshCrystal(String sessionId, HostSystem hostSystem) {
        JSch jsch = new JSch();
        hostSystem.setStatusCd(HostSystem.SUCCESS_STATUS);
        try {
            if (hostSystem.getCredential() == null) {
                return;
            }
            Session session = jsch.getSession(hostSystem.getLoginUsername(), hostSystem.getHost(),
                    hostSystem.getSshPortOrDefault());
            setSshCredential(hostSystem, jsch, session);
            // 默认设置
            SessionConfigUtils.defaultConnect(session);
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            ChannelShellUtils.setDefault(channel);
            setChannelPtySize(channel, hostSystem.getTerminalSize());
            // new session output
            SessionOutput sessionOutput = new SessionOutput(sessionId, hostSystem);
            // 启动线程处理会话
            Runnable run = new WatchSshCrystalTerminalOutputTask(sessionOutput, channel.getInputStream(),
                    hostSystem.getAuditPath());
            // JDK21 VirtualThreads
            Thread.ofVirtual()
                    .start(run);
            OutputStream inputToChannel = channel.getOutputStream();
            JSchSession jSchSession = JSchSession.builder()
                    .sessionId(sessionId)
                    .instanceId(hostSystem.getInstanceId())
                    .commander(new PrintStream(inputToChannel, true))
                    .inputToChannel(inputToChannel)
                    .channel(channel)
                    .hostSystem(hostSystem)
                    .build();
            jSchSession.setSessionOutput(sessionOutput);
            JSchSessionHolder.addSession(jSchSession);
            channel.connect();
        } catch (Exception e) {
            log.debug(e.getMessage());
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
        }
    }


    @SuppressWarnings("SpellCheckingInspection")
    public static void openSSHServer(String sessionId, HostSystem hostSystem, OutputStream out) throws SshException {
        JSch jsch = new JSch();
        hostSystem.setStatusCd(HostSystem.SUCCESS_STATUS);
        try {
            if (hostSystem.getCredential() == null) {
                return;
            }
            Session session = jsch.getSession(hostSystem.getLoginUsername(), hostSystem.getHost(),
                    hostSystem.getSshPortOrDefault());
            // 设置凭据
            setSshCredential(hostSystem, jsch, session);
            // 默认设置
            SessionConfigUtils.defaultConnect(session);
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            ChannelShellUtils.setDefault(channel);
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
     * 代理转发
     *
     * @param sessionId
     * @param proxyHost
     * @param targetHost
     * @param out
     * @throws SshException
     */
    public static void openSSHServer(String sessionId, HostSystem proxyHost, HostSystem targetHost,
                                     OutputStream out) throws SshException {
        // 为代理和目标分别创建独立的JSch实例，避免密钥冲突
        JSch proxyJsch = new JSch();
        JSch targetJsch = new JSch();

        proxyHost.setStatusCd(HostSystem.SUCCESS_STATUS);
        targetHost.setStatusCd(HostSystem.SUCCESS_STATUS);
        Session proxySession = null;
        Session targetSession = null;
        ChannelShell channel = null;
        try {
            if (proxyHost.getCredential() == null) {
                log.error("Proxy host credential is null for session: {}", sessionId);
                return;
            }
            if (targetHost.getCredential() == null) {
                log.error("Target host credential is null for session: {}", sessionId);
                return;
            }
            log.info("Establishing SSH proxy connection via {}@{}:{} to target {}@{}:{}", proxyHost.getLoginUsername(),
                    proxyHost.getHost(), proxyHost.getPort(), targetHost.getLoginUsername(), targetHost.getHost(),
                    targetHost.getPort());

            // 1. 设置代理服务器的Session - 使用独立的JSch实例
            proxySession = proxyJsch.getSession(proxyHost.getLoginUsername(), proxyHost.getHost(),
                    proxyHost.getSshPortOrDefault());

            // 设置代理服务器凭据
            setSshCredential(proxyHost, proxyJsch, proxySession);
            SessionConfigUtils.defaultConnect(proxySession);
            // proxySession.connect();
            log.info("Proxy connection established successfully");

            // 2. 通过代理连接目标服务器 - 使用端口转发
            int targetPort = targetHost.getSshPortOrDefault();
            int localPort = proxySession.setPortForwardingL(0, targetHost.getHost(), targetPort);
            log.debug("Port forwarding established: localhost:{} -> {}:{}", localPort, targetHost.getHost(),
                    targetPort);

            // 通过本地端口转发连接目标服务器 - 使用独立的JSch实例
            targetSession = targetJsch.getSession(targetHost.getLoginUsername(), LOCALHOST, localPort);

            // 设置目标服务器凭据
            setSshCredential(targetHost, targetJsch, targetSession);
            // 默认设置
            SessionConfigUtils.defaultConnect(targetSession);
            // targetSession.connect();
            log.info("Target connection established via proxy");

            // 3. 创建Shell通道
            channel = (ChannelShell) targetSession.openChannel("shell");
            ChannelShellUtils.setDefault(channel);

            // 初始化PtySize
            setChannelPtySize(channel, targetHost.getTerminalSize());

            // 4. 设置会话输出处理
            SessionOutput sessionOutput = new SessionOutput(sessionId, targetHost);
            // 启动线程处理会话
            Runnable run = new WatchSshServerOutputTask(sessionOutput, channel.getInputStream(), out,
                    targetHost.getAuditPath());
            // JDK21 VirtualThreads
            Thread.ofVirtual()
                    .name("ssh-proxy-output-" + sessionId)
                    .start(run);

            // 5. 创建JSchSession并保存代理会话引用
            OutputStream inputToChannel = channel.getOutputStream();
            JSchSession jSchSession = JSchSession.builder()
                    .sessionId(sessionId)
                    .instanceId(targetHost.getInstanceId())
                    .commander(new PrintStream(inputToChannel, true, StandardCharsets.UTF_8))
                    .inputToChannel(inputToChannel)
                    .channel(channel)
                    .hostSystem(targetHost)
                    .proxySession(proxySession)  // 将代理Session存储到JSchSession中
                    .build();
            jSchSession.setSessionOutput(sessionOutput);
            JSchSessionHolder.addSession(jSchSession);
            channel.connect();
            log.info("SSH proxy connection established successfully for session: {}", sessionId);
        } catch (Exception e) {
            log.error("Failed to establish SSH proxy connection for session: {}", sessionId, e);
            // 清理资源
            cleanupProxyResources(proxySession, targetSession, channel);
            // 处理异常状态
            handleProxyConnectionException(e, proxyHost, targetHost);
            throw new SshException("SSH proxy connection failed: " + e.getMessage());
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

    /**
     * 清理代理连接相关资源
     */
    private static void cleanupProxyResources(Session proxySession, Session targetSession, ChannelShell channel) {
        // 关闭通道
        if (channel != null && channel.isConnected()) {
            try {
                channel.disconnect();
                log.debug("Channel disconnected");
            } catch (Exception e) {
                log.warn("Error disconnecting channel", e);
            }
        }

        // 关闭目标会话
        if (targetSession != null && targetSession.isConnected()) {
            try {
                targetSession.disconnect();
                log.debug("Target session disconnected");
            } catch (Exception e) {
                log.warn("Error disconnecting target session", e);
            }
        }

        // 关闭代理会话
        if (proxySession != null && proxySession.isConnected()) {
            try {
                proxySession.disconnect();
                log.debug("Proxy session disconnected");
            } catch (Exception e) {
                log.warn("Error disconnecting proxy session", e);
            }
        }
    }

    /**
     * 处理代理连接异常
     */
    private static void handleProxyConnectionException(Exception e, HostSystem proxyHost, HostSystem targetHost) {
        String errorMsg = e.getMessage()
                .toLowerCase();

        // 处理目标主机异常状态
        if (errorMsg.contains("userauth fail")) {
            targetHost.setStatusCd(HostSystem.PUBLIC_KEY_FAIL_STATUS);
        } else if (errorMsg.contains("auth fail") || errorMsg.contains("auth cancel")) {
            targetHost.setStatusCd(HostSystem.AUTH_FAIL_STATUS);
        } else if (errorMsg.contains("unknownhostexception")) {
            targetHost.setErrorMsg("DNS Lookup Failed");
            targetHost.setStatusCd(HostSystem.HOST_FAIL_STATUS);
        } else if (errorMsg.contains("connection refused")) {
            targetHost.setErrorMsg("Connection Refused");
            targetHost.setStatusCd(HostSystem.HOST_FAIL_STATUS);
        } else if (errorMsg.contains("timeout")) {
            targetHost.setErrorMsg("Connection Timeout");
            targetHost.setStatusCd(HostSystem.HOST_FAIL_STATUS);
        } else {
            targetHost.setStatusCd(HostSystem.GENERIC_FAIL_STATUS);
        }

        // 如果是代理连接阶段的错误，也设置代理主机状态
        if (errorMsg.contains("proxy") || !proxyHost.getStatusCd()
                .equals(HostSystem.SUCCESS_STATUS)) {
            if (errorMsg.contains("userauth fail")) {
                proxyHost.setStatusCd(HostSystem.PUBLIC_KEY_FAIL_STATUS);
            } else if (errorMsg.contains("auth fail") || errorMsg.contains("auth cancel")) {
                proxyHost.setStatusCd(HostSystem.AUTH_FAIL_STATUS);
            } else if (errorMsg.contains("unknownhostexception")) {
                proxyHost.setErrorMsg("Proxy DNS Lookup Failed");
                proxyHost.setStatusCd(HostSystem.HOST_FAIL_STATUS);
            } else {
                proxyHost.setStatusCd(HostSystem.GENERIC_FAIL_STATUS);
            }
        }
    }

}
