package com.baiyi.cratos.shell.commands.custom.eds;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.shell.*;
import com.baiyi.cratos.shell.annotation.ClearScreen;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import com.baiyi.cratos.shell.commands.AbstractCommand;
import com.baiyi.cratos.shell.commands.SshShellComponent;
import com.baiyi.cratos.shell.context.ComputerAssetContext;
import com.baiyi.cratos.shell.util.TerminalUtil;
import com.baiyi.cratos.ssh.core.auditor.ServerCommandAuditor;
import com.baiyi.cratos.ssh.core.builder.HostSystemBuilder;
import com.baiyi.cratos.ssh.core.builder.SshSessionInstanceBuilder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SshSessionFacade;
import com.baiyi.cratos.ssh.core.handler.RemoteInvokeHandler;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.core.model.JSchSession;
import com.baiyi.cratos.ssh.core.model.JSchSessionHolder;
import com.baiyi.cratos.ssh.core.model.SshSessionIdMapper;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.SshException;
import org.apache.sshd.common.channel.ChannelOutputStream;
import org.apache.sshd.server.session.ServerSession;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static com.baiyi.cratos.shell.commands.custom.eds.EdsCloudComputerListCommand.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午5:18
 * &#064;Version 1.0
 */
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds Computer Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsCloudComputerLoginCommand extends AbstractCommand {

    private final SshSessionFacade sshSessionFacade;

    private final CredentialService credentialService;

    private final SshAuditProperties sshAuditProperties;

    private final ServerCommandAuditor serverCommandAuditor;

    public static final String GROUP = "computer";
    private static final String COMMAND_COMPUTER_LOGIN = GROUP + "-login";

    public EdsCloudComputerLoginCommand(SshShellHelper helper, SshShellProperties properties,
                                        SshSessionFacade sshSessionFacade, CredentialService credentialService,
                                        SshAuditProperties sshAuditProperties,
                                        ServerCommandAuditor serverCommandAuditor) {
        super(helper, properties, properties.getCommands()
                .getComputer());
        this.sshSessionFacade = sshSessionFacade;
        this.credentialService = credentialService;
        this.sshAuditProperties = sshAuditProperties;
        this.serverCommandAuditor = serverCommandAuditor;
    }

    @ClearScreen
    @ShellMethod(key = {COMMAND_COMPUTER_LOGIN, "cl"}, value = "Login to the computer.")
    @ShellAuthentication(resource = "/application/app-grouping")
    public void login(@ShellOption(help = "ID", defaultValue = "1") int id,
                      @ShellOption(help = "Account", defaultValue = "") String account) {
        ServerSession serverSession = helper.getSshSession();
        final String sessionId = SshSessionIdMapper.getSessionId(serverSession.getIoSession());
        // 从上下文中取出
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        Terminal terminal = sshContext.getTerminal();
        ChannelOutputStream out = (ChannelOutputStream) sshContext.getSshShellRunnable()
                .getOs();
        Map<Integer, EdsAsset> computerMapper = ComputerAssetContext.getComputerContext();
        EdsAsset edsAsset = computerMapper.get(id);
        if (edsAsset == null) {
            helper.print("Computer does not exist.", PromptColor.RED);
            return;
        }
        final String sshSessionInstanceId = generateInstanceId(edsAsset);
        if (!ComputerAssetContext.getAccountContext()
                .containsKey(account)) {
            helper.print("Account does not exist.", PromptColor.RED);
            return;
        }
        ServerAccount serverAccount = ComputerAssetContext.getAccountContext()
                .get(account);
        Credential credential = credentialService.getById(serverAccount.getCredentialId());
        if (credential == null) {
            helper.print("Account not configured with credentials.", PromptColor.RED);
            return;
        }
        try {
            final String auditPath = sshAuditProperties.generateAuditLogFilePath(sessionId, sshSessionInstanceId);
            HostSystem hostSystem = HostSystemBuilder.buildHostSystem(edsAsset, serverAccount, credential);
            hostSystem.setInstanceId(sshSessionInstanceId);
            hostSystem.setTerminalSize(helper.terminalSize());
            hostSystem.setAuditPath(auditPath);
            SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(sessionId, hostSystem,
                    SshSessionInstanceTypeEnum.COMPUTER, auditPath);

            sshSessionFacade.addSshSessionInstance(sshSessionInstance);
            // open ssh
            RemoteInvokeHandler.openSSHServer(sessionId, hostSystem, out);
            TerminalUtil.enterRawMode(terminal);
            // 无延迟
            out.setNoDelay(true);
            Size size = terminal.getSize();
            try {
                while (true) {
                    if (isClosed(sessionId, sshSessionInstanceId) || serverSession.isClosed()) {
                        TimeUtil.millisecondsSleep(150L);
                        break;
                    }
                    doResize(size, terminal, sessionId, sshSessionInstanceId);
                    int input = terminal.reader()
                            .read(5L);
                    send(sessionId, sshSessionInstanceId, input);
                }
            } catch (Exception e) {
                // printLogout("Server connection disconnected, session duration %s/s", inst1);
            } finally {
                sshSessionFacade.closeSshSessionInstance(sshSessionInstance);
            }
        } catch (SshException e) {
            String msg = StringFormatter.format("SSH connection error: {}", e.getMessage());
            log.error(msg);
            helper.print(msg, PromptColor.RED);
        } finally {
            serverCommandAuditor.asyncRecordCommand(sessionId, sshSessionInstanceId);
            JSchSessionHolder.closeSession(sessionId, sshSessionInstanceId);
        }
    }

    private String generateInstanceId(EdsAsset computerAsset) {
        return Joiner.on("#")
                .join(computerAsset.getAssetId(), UUID.randomUUID());
    }

    private void doResize(Size size, Terminal terminal, String sessionId, String instanceId) {
        if (!terminal.getSize()
                .equals(size)) {
            size = terminal.getSize();
            TerminalUtil.resize(sessionId, instanceId, size);
        }
    }

    /**
     * 打印会话关闭信息
     *
     * @param logout
     * @param instant
     */
    private void printLogout(String logout, Instant instant) {
        helper.print(String.format(logout, Duration.between(instant, Instant.now())
                .getSeconds()), PromptColor.RED);
    }

    private boolean isClosed(String sessionId, String instanceId) {
        JSchSession jSchSession = JSchSessionHolder.getBySessionId(sessionId, instanceId);
        assert jSchSession != null;
        return jSchSession.getChannel()
                .isClosed();
    }

    private void send(String sessionId, String instanceId, int input) throws Exception {
        if (input < 0) {
            return;
        }
        JSchSession jSchSession = JSchSessionHolder.getBySessionId(sessionId, instanceId);
        if (jSchSession == null) {
            throw new Exception();
        }
        char ch = (char) input;
        jSchSession.getCommander()
                .print(ch);
    }

}