package com.baiyi.cratos.shell.commands.custom.eds;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesPodRepo;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.shell.*;
import com.baiyi.cratos.shell.annotation.ClearScreen;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import com.baiyi.cratos.shell.commands.AbstractCommand;
import com.baiyi.cratos.shell.commands.SshShellComponent;
import com.baiyi.cratos.shell.context.PodAssetContext;
import com.baiyi.cratos.shell.util.TerminalUtil;
import com.baiyi.cratos.ssh.core.auditor.PodCommandAuditor;
import com.baiyi.cratos.ssh.core.builder.SshSessionInstanceBuilder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.model.PodAssetModel;
import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.model.SshSessionIdMapper;
import com.baiyi.cratos.ssh.core.watch.ssh.WatchKubernetesExecShellOutputTask;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.sshd.common.channel.ChannelOutputStream;
import org.apache.sshd.server.session.ServerSession;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.baiyi.cratos.shell.commands.custom.eds.EdsCloudComputerListCommand.GROUP;
import static com.baiyi.cratos.ssh.core.util.ChannelShellUtil.DEF_UNICODE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/28 上午10:10
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds Kubernetes Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsKubernetesPodCommand extends AbstractCommand {

    public static final String GROUP = "kubernetes-pod";

    private static final String COMMAND_POD_LIST = GROUP + "-list";

    private static final String COMMAND_POD_LOGIN = GROUP + "-login";

    private static final String COMMAND_POD_VIEW_LOG = GROUP + "-view-log";

    private final EdsInstanceService edsInstanceService;

    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    private final KubernetesPodRepo kubernetesPodRepo;

    private final KubernetesClientBuilder kubernetesClientBuilder;

    private final SimpleSshSessionFacade simpleSshSessionFacade;

    private final SshAuditProperties sshAuditProperties;

    private final PodCommandAuditor podCommandAuditor;

    public final static String[] TABLE_FIELD_NAME = {"ID", "Eds Instance", "Namespace", "Deployment", "Containers"};

    /**
     * ^C
     */
    private static final int QUIT = 3;
    private static final int EOF = 4;

    public EdsKubernetesPodCommand(SshShellHelper helper, SshShellProperties properties,
                                   EdsInstanceService edsInstanceService,
                                   EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                   KubernetesPodRepo kubernetesPodRepo, KubernetesClientBuilder kubernetesClientBuilder,
                                   SimpleSshSessionFacade simpleSshSessionFacade, SshAuditProperties sshAuditProperties,
                                   PodCommandAuditor podCommandAuditor) {
        super(helper, properties, properties.getCommands()
                .getAsset());
        this.edsInstanceService = edsInstanceService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.kubernetesPodRepo = kubernetesPodRepo;
        this.kubernetesClientBuilder = kubernetesClientBuilder;
        this.simpleSshSessionFacade = simpleSshSessionFacade;
        this.sshAuditProperties = sshAuditProperties;
        this.podCommandAuditor = podCommandAuditor;
    }

    @ShellMethod(key = COMMAND_POD_LIST, value = "List pod")
    public void podList(@ShellOption(help = "Eds Kubernetes Instance Name", defaultValue = "") String edsInstanceName,
                        @ShellOption(help = "Namespace", defaultValue = "") String namespace,
                        @ShellOption(help = "Deployment Name", defaultValue = "") String name) {
        PrettyTable podTable = PrettyTable.fieldNames(TABLE_FIELD_NAME);
        EdsInstance uniqueKey = EdsInstance.builder()
                .instanceName(edsInstanceName)
                .build();

        EdsInstance edsInstance = edsInstanceService.getByUniqueKey(uniqueKey);
        if (edsInstance == null) {
            helper.printError(StringFormatter.format("Eds instance {} does not exist.", edsInstanceName));
            return;
        }
        if (!EdsInstanceTypeEnum.KUBERNETES.name()
                .equals(edsInstance.getEdsType())) {
            helper.printError("Eds instance incorrect type.");
            return;
        }
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> edsInstanceProviderHolder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                edsInstance.getId(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        EdsKubernetesConfigModel.Kubernetes kubernetes = edsInstanceProviderHolder.getInstance()
                .getEdsConfigModel();
        List<Pod> pods = kubernetesPodRepo.list(kubernetes, namespace, name);

        Map<Integer, PodAssetModel> podContext = Maps.newHashMap();

        int i = 1;
        for (Pod pod : pods) {
            final String containers = Joiner.on(" ")
                    .skipNulls()
                    .join(pod.getSpec()
                            .getContainers()
                            .stream()
                            .map(Container::getName)
                            .toList());
            podTable.addRow(i, edsInstanceName, namespace, name, containers);
            // context
            PodAssetModel podAssetModel = PodAssetModel.builder()
                    .pod(pod)
                    .build();

            podContext.put(i, podAssetModel);
            i++;
        }
        PodAssetContext.setContext(podContext, kubernetes);
        helper.print(podTable.toString());
    }

    @ClearScreen
    @ShellMethod(key = {COMMAND_POD_LOGIN, "pl"}, value = "Login to the pod(container).")
    @ShellAuthentication(resource = "/pod/login")
    public void podLogin(@ShellOption(help = "ID", defaultValue = "1") int id,
                         @ShellOption(help = "Container", defaultValue = "") String container) {
        // 从上下文中取出
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        Terminal terminal = sshContext.getTerminal();
        Map<Integer, PodAssetModel> podContext = PodAssetContext.getPodContext();
        PodAssetModel podAssetModel = podContext.get(id);
        EdsKubernetesConfigModel.Kubernetes kubernetes = PodAssetContext.getConfigContext();
        if (StringUtils.hasText(container)) {
            final String findContainer = container;
            if (podAssetModel.getPod()
                    .getSpec()
                    .getContainers()
                    .stream()
                    .noneMatch(e -> findContainer.equals(e.getName()))) {
                helper.print(StringFormatter.format("The container name '{}' you specified does not exist.", container),
                        PromptColor.YELLOW);
                return;
            }
        } else {
            container = podAssetModel.getPod()
                    .getSpec()
                    .getContainers()
                    .getFirst()
                    .getName();
        }

        KubernetesPodRepo.SimpleListener listener = new KubernetesPodRepo.SimpleListener();
        ServerSession serverSession = helper.getSshSession();
        final String sessionId = SshSessionIdMapper.getSessionId(serverSession.getIoSession());
        String sshSessionInstanceId = generateInstanceId(podAssetModel, container);
        podAssetModel.setInstanceId(sshSessionInstanceId);
        Size size = terminal.getSize();
        try {
            final String auditPath = sshAuditProperties.generateAuditLogFilePath(sessionId, sshSessionInstanceId);
            SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(sessionId, podAssetModel,
                    SshSessionInstanceTypeEnum.CONTAINER_SHELL, auditPath);
            simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); KubernetesClient kc = kubernetesClientBuilder.build(
                    kubernetes); ExecWatch execWatch = kc.pods()
                    .inNamespace(podAssetModel.acqNamespace())
                    .withName(podAssetModel.acqName())
                    .inContainer(container)
                    .redirectingInput()
                    //.redirectingOutput()
                    //.redirectingError()
                    //.redirectingErrorChannel()
                    .writingOutput(baos)
                    .withTTY()
                    .usingListener(listener)
                    // .exec("env", "TERM=xterm", "COLUMNS=" + columns, "LINES=" + lines, "sh", "-c", "ls -la");
                    .exec("env", "TERM=xterm", "LANG=" + DEF_UNICODE, "COLUMNS=" + size.getColumns(),
                            "LINES=" + size.getRows(), "sh")) {
                //execWatch.resize(size.getColumns(), size.getRows());
                SessionOutput sessionOutput = new SessionOutput(sessionId, sshSessionInstanceId);
                ChannelOutputStream out = (ChannelOutputStream) sshContext.getSshShellRunnable()
                        .getOs();
                out.setNoDelay(true);
                WatchKubernetesExecShellOutputTask run = new WatchKubernetesExecShellOutputTask(sessionOutput, baos,
                        auditPath, out);
                // JDK21 VirtualThreads
                Thread.ofVirtual()
                        .start(run);
                // 行模式
                TerminalUtil.enterRawMode(terminal);
                while (!listener.isClosed() || serverSession.isClosed()) {
                    int input = terminal.reader()
                            .read(5L);
                    if (input >= 0) {
                        // execWatch.getInput().write(input);
                        execWatch.getInput()
                                .write(Character.toString((char) input)
                                        .getBytes(StandardCharsets.UTF_8));
                        execWatch.getInput()
                                .flush();
                        if (input == EOF) {
                            // 等待退出，避免sh残留
                            TimeUtil.millisecondsSleep(200L);
                            break;
                        }
                    }
                    tryResize(size, terminal, execWatch);
                }
            } catch (Exception e) {
                log.debug(e.getMessage());
                helper.print(e.getMessage(), PromptColor.RED);
            } finally {
                simpleSshSessionFacade.closeSshSessionInstance(sshSessionInstance);
            }
        } catch (Exception ignored) {
        } finally {
            podCommandAuditor.asyncRecordCommand(sessionId, sshSessionInstanceId);
        }
    }

    @ClearScreen
    @ShellMethod(value = "Show kubernetes pod container log [Enter ctrl+c to exit the session]", key = {COMMAND_POD_VIEW_LOG})
    @ShellAuthentication(resource = "/pod/view/log")
    public void podLog(@ShellOption(help = "ID", defaultValue = "") Integer id,
                       @ShellOption(help = "Container", defaultValue = "") String container,
                       @ShellOption(help = "Tailing Lines", defaultValue = "100") int lines) {
        // 从上下文中取出
        SshContext sshContext = SshShellCommandFactory.SSH_THREAD_CONTEXT.get();
        Terminal terminal = sshContext.getTerminal();
        Map<Integer, PodAssetModel> podContext = PodAssetContext.getPodContext();
        PodAssetModel podAssetModel = podContext.get(id);
        if (StringUtils.hasText(container)) {
            final String findContainer = container;
            if (podAssetModel.getPod()
                    .getSpec()
                    .getContainers()
                    .stream()
                    .noneMatch(e -> findContainer.equals(e.getName()))) {
                helper.print(StringFormatter.format("The container name '{}' you specified does not exist.", container),
                        PromptColor.YELLOW);
                return;
            }
        } else {
            container = podAssetModel.getPod()
                    .getSpec()
                    .getContainers()
                    .getFirst()
                    .getName();
        }
        EdsKubernetesConfigModel.Kubernetes kubernetes = PodAssetContext.getConfigContext();
        ServerSession serverSession = helper.getSshSession();
        final String sessionId = SshSessionIdMapper.getSessionId(serverSession.getIoSession());
        String sshSessionInstanceId = generateInstanceId(podAssetModel, container);
        podAssetModel.setInstanceId(sshSessionInstanceId);
        final String auditPath = sshAuditProperties.generateAuditLogFilePath(sessionId, sshSessionInstanceId);
        try {
            SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(sessionId, podAssetModel,
                    SshSessionInstanceTypeEnum.CONTAINER_SHELL, auditPath);
            simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
            try (KubernetesClient kc = kubernetesClientBuilder.build(
                    kubernetes); ByteArrayOutputStream baos = new ByteArrayOutputStream(); LogWatch logWatch = kc.pods()
                    .inNamespace(podAssetModel.acqNamespace())
                    .withName(podAssetModel.acqName())
                    .inContainer(container)
                    .tailingLines(lines)
                    .watchLog(baos);) {
                TerminalUtil.enterRawMode(terminal);
                SessionOutput sessionOutput = new SessionOutput(sessionId, sshSessionInstanceId);
                // 高速输出日志流
                WatchKubernetesExecShellOutputTask run = new WatchKubernetesExecShellOutputTask(sessionOutput, baos,
                        auditPath, sshContext.getSshShellRunnable()
                        .getOs());
                Thread.ofVirtual()
                        .start(run);
                while (true) {
                    int ch = terminal.reader()
                            .read(25L);
                    if (ch != -2) {
                        if (ch == QUIT) {
                            run.close();
                            break;
                        } else {
                            terminal.writer()
                                    .print(helper.getColored("\nInput [ ctrl+c ] end viewing logs.\n",
                                            PromptColor.GREEN));
                            terminal.writer()
                                    .flush();
                            TimeUtil.millisecondsSleep(200L);
                        }
                    }
                    TimeUtil.millisecondsSleep(25L);
                }
            } catch (Exception e) {
                log.debug(e.getMessage());
            } finally {
                simpleSshSessionFacade.closeSshSessionInstance(sshSessionInstance);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        } finally {
            // podCommandAuditor.asyncRecordCommand(sessionId, sshSessionInstanceId);
        }
    }

    /**
     * podName#
     *
     * @param podAssetModel
     * @param container
     * @return
     */
    private String generateInstanceId(PodAssetModel podAssetModel, String container) {
        return Joiner.on("#")
                .join(podAssetModel.getPod()
                        .getMetadata()
                        .getName(), container, UUID.randomUUID());
    }

    private void tryResize(Size size, Terminal terminal, ExecWatch execWatch) {
        if (terminal.getSize()
                .equals(size)) {
            return;
        }
        size = terminal.getSize();
        execWatch.resize(size.getColumns(), size.getRows());
    }

}