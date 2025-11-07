package com.baiyi.cratos.shell.command.custom.eds;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.common.util.SshIdUtils;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.common.util.UserDisplayUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.BusinessDocFacade;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobotModel;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.shell.*;
import com.baiyi.cratos.shell.annotation.ClearScreen;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.baiyi.cratos.shell.command.custom.eds.handler.WatchTerminalSignalHandler;
import com.baiyi.cratos.shell.context.ComputerAssetContext;
import com.baiyi.cratos.shell.util.MyLineMarkdownUtils;
import com.baiyi.cratos.shell.util.TerminalUtils;
import com.baiyi.cratos.ssh.core.auditor.ServerCommandAuditor;
import com.baiyi.cratos.ssh.core.builder.HostSystemBuilder;
import com.baiyi.cratos.ssh.core.builder.SshSessionInstanceBuilder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
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
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.SSHSERVER_USER_LOGIN_SERVER_NOTICE;
import static com.baiyi.cratos.common.enums.SysTagKeys.INSPECTION_NOTIFICATION;
import static com.baiyi.cratos.shell.command.custom.eds.EdsComputerLoginCommand.GROUP;


/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/10 11:36
 * &#064;Version 1.0
 */
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds Computer Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsComputerLoginCommand extends AbstractCommand {

    private final SimpleSshSessionFacade simpleSshSessionFacade;
    private final CredentialService credentialService;
    private final SshAuditProperties sshAuditProperties;
    private final ServerCommandAuditor serverCommandAuditor;
    private final BusinessDocFacade businessDocFacade;
    private final BusinessTagFacade businessTagFacade;

    public static final String GROUP = "computer";
    private static final String COMMAND_COMPUTER_LOGIN = GROUP + "-login";

    private final EdsAssetService edsAssetService;
    private final ServerAccountService serverAccountService;
    private final EdsInstanceHelper edsInstanceHelper;
    private final EdsConfigService edsConfigService;
    private final DingtalkService dingtalkService;
    private final NotificationTemplateService notificationTemplateService;
    private final UserService userService;

    @Value("${cratos.notification:NORMAL}")
    private String notification;
    @Value("${cratos.language:en-us}")
    protected String language;

    public EdsComputerLoginCommand(SshShellHelper helper, SshShellProperties properties,
                                   SimpleSshSessionFacade simpleSshSessionFacade, CredentialService credentialService,
                                   SshAuditProperties sshAuditProperties, ServerCommandAuditor serverCommandAuditor,
                                   BusinessDocFacade businessDocFacade, BusinessTagFacade businessTagFacade,
                                   EdsAssetService edsAssetService, ServerAccountService serverAccountService,
                                   EdsInstanceHelper edsInstanceHelper, EdsConfigService edsConfigService,
                                   DingtalkService dingtalkService,
                                   NotificationTemplateService notificationTemplateService, UserService userService) {
        super(helper, properties, properties.getCommands()
                .getComputer());
        this.simpleSshSessionFacade = simpleSshSessionFacade;
        this.credentialService = credentialService;
        this.sshAuditProperties = sshAuditProperties;
        this.serverCommandAuditor = serverCommandAuditor;
        this.businessDocFacade = businessDocFacade;
        this.businessTagFacade = businessTagFacade;
        this.edsAssetService = edsAssetService;
        this.serverAccountService = serverAccountService;
        this.edsInstanceHelper = edsInstanceHelper;
        this.edsConfigService = edsConfigService;
        this.dingtalkService = dingtalkService;
        this.notificationTemplateService = notificationTemplateService;
        this.userService = userService;
    }

    @ClearScreen
    @ShellMethod(key = {COMMAND_COMPUTER_LOGIN, "cl", "login"}, value = "Open to the computer.")
    @ShellAuthentication(resource = "/computer/login")
    public void computerLogin(@ShellOption(help = "ID", defaultValue = "1") int id,
                              @ShellOption(help = "Account", defaultValue = "") String account,
                              @ShellOption(help = "Preview Docs", defaultValue = "false") boolean proxy,
                              @ShellOption(help = "Preview Docs", defaultValue = "false") boolean docs) {
        Map<Integer, EdsAsset> computerMapper = ComputerAssetContext.getComputerContext();
        if (CollectionUtils.isEmpty(computerMapper) || !computerMapper.containsKey(id)) {
            helper.print("Computer does not exist, exec computer-list first, then login", PromptColor.RED);
            return;
        }
        EdsAsset asset = computerMapper.get(id);
        // Preview Docs
        if (docs) {
            previewDocs(asset);
            return;
        }
        final String sshSessionInstanceId = generateInstanceId(asset);
        if (!StringUtils.hasText(account)) {
            account = getServerAccount(asset);
        }
        if (!ComputerAssetContext.getAccountContext()
                .containsKey(account)) {
            helper.print("Account does not exist.", PromptColor.RED);
            return;
        }
        ServerSession serverSession = helper.getSshSession();
        final String sessionId = SshSessionIdMapper.getSessionId(serverSession.getIoSession());
        // 从上下文中取出
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        Terminal terminal = sshContext.getTerminal();
        ChannelOutputStream out = (ChannelOutputStream) sshContext.getSshShellRunnable()
                .getOs();
        ServerAccount serverAccount = ComputerAssetContext.getAccountContext()
                .get(account);
        Credential credential = credentialService.getById(serverAccount.getCredentialId());
        if (credential == null) {
            helper.print("Account not configured with credentials.", PromptColor.RED);
            return;
        }
        try {
            final String auditPath = sshAuditProperties.generateAuditLogFilePath(sessionId, sshSessionInstanceId);
            HostSystem targetSystem = HostSystemBuilder.buildHostSystem(sshSessionInstanceId, asset, serverAccount,
                    credential);
            // targetSystem.setInstanceId(sshSessionInstanceId);
            targetSystem.setTerminalSize(helper.terminalSize());
            targetSystem.setAuditPath(auditPath);

            SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(sessionId, targetSystem,
                    SshSessionInstanceTypeEnum.COMPUTER, auditPath);
            // Watch signal
            WatchTerminalSignalHandler watchTerminalSignalHandler = new WatchTerminalSignalHandler(sessionId,
                    sshSessionInstanceId, terminal);
            Terminal.SignalHandler prevHandler = terminal.handle(Terminal.Signal.WINCH, watchTerminalSignalHandler);
            try {
                simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
                try {
                    User user = userService.getByUsername(helper.getSshSession()
                            .getUsername());
                    DingtalkRobotModel.Msg msg = getMsg(user, serverAccount.getUsername(), asset.getAssetKey(),
                            asset.getName());
                    sendUserLoginServerNotice(msg);
                } catch (IOException ignored) {
                }
                // open ssh
                if (proxy) {
                    HostSystem proxySystem = getProxyHost(asset);
                    if (Objects.isNull(proxySystem)) {
                        RemoteInvokeHandler.openSSHServer(sessionId, targetSystem, out);
                    } else {
                        // 代理访问
                        RemoteInvokeHandler.openSSHServer(sessionId, proxySystem, targetSystem, out);
                    }
                } else {
                    RemoteInvokeHandler.openSSHServer(sessionId, targetSystem, out);
                }
                TerminalUtils.enterRawMode(terminal);
                // 无延迟
                out.setNoDelay(true);
                while (true) {
                    if (isClosed(sessionId, sshSessionInstanceId) || serverSession.isClosed()) {
                        TimeUnit.MILLISECONDS.sleep(150L);
                        break;
                    }
                    //  tryResize(size, terminal, sessionId, sshSessionInstanceId);
                    int input = terminal.reader()
                            .read(5L);
                    send(sessionId, sshSessionInstanceId, input);
                }
            } catch (InterruptedException e) {
                Thread.currentThread()
                        .interrupt();
            } catch (IOException ioException) {
                log.debug(ioException.getMessage(), ioException);
            } finally {
                if (prevHandler != null) {
                    terminal.handle(Terminal.Signal.WINCH, prevHandler);
                }
                simpleSshSessionFacade.closeSshSessionInstance(sshSessionInstance);
                serverCommandAuditor.asyncRecordCommand(sessionId, sshSessionInstanceId);
            }
        } catch (SshException ex) {
            log.debug(ex.getMessage());
            helper.print(ex.getMessage(), PromptColor.RED);
        } finally {
            JSchSessionHolder.closeSession(sessionId, sshSessionInstanceId);
        }
    }

    private HostSystem getProxyHost(EdsAsset targetComputer) throws SshException {
        BusinessTag sshProxyBusinessTag = businessTagFacade.getBusinessTag(SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(targetComputer.getId())
                .build(), SysTagKeys.SSH_PROXY.getKey());
        if (Objects.isNull(sshProxyBusinessTag)) {
            return HostSystem.NO_HOST;
        }
        // 搜索资产
        String proxyIP = sshProxyBusinessTag.getTagValue();
        if (!IpUtils.isIP(proxyIP)) {
            return HostSystem.NO_HOST;
        }
        List<EdsAsset> proxyComputers = edsAssetService.queryInstanceAssetByTypeAndKey(targetComputer.getInstanceId(),
                targetComputer.getAssetType(), proxyIP);
        if (CollectionUtils.isEmpty(proxyComputers)) {
            return HostSystem.NO_HOST;
        }
        EdsAsset proxyComputer = proxyComputers.getFirst();
        BusinessTag serverAccountTag = businessTagFacade.getBusinessTag(SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(proxyComputer.getId())
                .build(), SysTagKeys.SERVER_ACCOUNT.getKey());
        if (!StringUtils.hasText(serverAccountTag.getTagValue())) {
            return HostSystem.NO_HOST;
        }
        ServerAccount serverAccount = serverAccountService.getByName(serverAccountTag.getTagValue());
        Credential credential = credentialService.getById(serverAccount.getCredentialId());
        return HostSystemBuilder.buildHostSystem(proxyComputer, serverAccount, credential);
    }

    private void previewDocs(EdsAsset asset) {
        BusinessParam.GetByBusiness hasBusiness = BusinessParam.GetByBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(asset.getId())
                .build();
        businessDocFacade.getBusinessTextDocByBusiness(hasBusiness)
                .forEach(doc -> {
                    helper.print("Document: " + doc.getName(), PromptColor.CYAN);
                    helper.print(
                            "--------------------------------------------------------------------------------------");
                    helper.print(MyLineMarkdownUtils.of(doc), PromptColor.BRIGHT);
                    helper.print(
                            "--------------------------------------------------------------------------------------");
                });
    }

    private String generateInstanceId(EdsAsset computerAsset) {
        return Joiner.on("#")
                .join(computerAsset.getAssetId(), SshIdUtils.generateID());
    }

    private boolean isClosed(String sessionId, String instanceId) {
        JSchSession jSchSession = JSchSessionHolder.getSession(sessionId, instanceId);
        if (jSchSession == null) {
            return true;
        }
        return jSchSession.getChannel()
                .isClosed();
    }

    private void send(String sessionId, String instanceId, int input) throws NullPointerException {
        if (input < 0) {
            return;
        }
        JSchSession jSchSession = JSchSessionHolder.getSession(sessionId, instanceId);
        if (jSchSession == null) {
            throw new NullPointerException("jSchSession is null.");
        }
        char ch = (char) input;
        jSchSession.getCommander()
                .print(ch);
    }

    private String getServerAccount(EdsAsset edsAsset) {
        BusinessTag serverAccountBusinessTag = getBusinessTag(edsAsset);
        return Objects.nonNull(serverAccountBusinessTag) ? serverAccountBusinessTag.getTagValue() : "";
    }

    private BusinessTag getBusinessTag(EdsAsset edsAsset) {
        return businessTagFacade.getBusinessTag(toHasBusiness(edsAsset), SysTagKeys.SERVER_ACCOUNT.getKey());
    }

    private BaseBusiness.HasBusiness toHasBusiness(EdsAsset edsAsset) {
        return SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(edsAsset.getId())
                .build();
    }

    protected DingtalkRobotModel.Msg getMsg(User loginUser, String serverAccount, String serverIP,
                                            String serverName) throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate();
        String msg = BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put("loginUser", UserDisplayUtils.getDisplayName(loginUser))
                .put("targetServer", Joiner.on("@")
                        .join(serverAccount, serverIP))
                .put("serverName", serverName)
                .put("loginTime", TimeUtils.parse(new Date(), Global.ISO8601))
                .build());
        return DingtalkRobotModel.loadAs(msg);
    }

    @SuppressWarnings("unchecked")
    private void sendUserLoginServerNotice(DingtalkRobotModel.Msg message) {
        List<EdsInstance> edsInstanceList = edsInstanceHelper.queryValidEdsInstance(EdsInstanceTypeEnum.DINGTALK_ROBOT,
                                                                                    INSPECTION_NOTIFICATION.getKey());
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send inspection notifications.");
            return;
        }
        List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>> holders = (List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>>) edsInstanceHelper.buildHolder(
                edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
        holders.forEach(providerHolder -> {
            EdsConfig edsConfig = edsConfigService.getById(providerHolder.getInstance()
                    .getEdsInstance()
                    .getConfigId());
            EdsDingtalkConfigModel.Robot robot = providerHolder.getProvider()
                    .configLoadAs(edsConfig);
            dingtalkService.send(robot.getToken(), message);
            providerHolder.importAsset(message);
        });
    }

    private NotificationTemplate getNotificationTemplate() {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(SSHSERVER_USER_LOGIN_SERVER_NOTICE.name())
                .lang(language)
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

}