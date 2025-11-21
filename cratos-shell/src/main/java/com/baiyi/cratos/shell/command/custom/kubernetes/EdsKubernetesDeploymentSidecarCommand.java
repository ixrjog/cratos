package com.baiyi.cratos.shell.command.custom.kubernetes;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.baiyi.cratos.shell.model.KubernetesDeploymentSidecarParam;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.baiyi.cratos.shell.command.custom.kubernetes.EdsKubernetesDeploymentListCommand.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/24 10:05
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds Kubernetes Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsKubernetesDeploymentSidecarCommand extends AbstractCommand {

    public static final String GROUP = "kubernetes-deployment";
    private static final String COMMAND_DEPLOYMENT_SIDECAR_COPY = GROUP + "-sidecar-copy";
    private static final String COMMAND_DEPLOYMENT_SIDECAR_REMOVE = GROUP + "-sidecar-remove";

    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;

    public EdsKubernetesDeploymentSidecarCommand(SshShellHelper helper, SshShellProperties properties,
                                                 EdsInstanceService edsInstanceService,
                                                 EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                 KubernetesDeploymentRepo kubernetesDeploymentRepo) {
        super(helper, properties, properties.getCommands()
                .getAsset());
        this.edsInstanceService = edsInstanceService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.kubernetesDeploymentRepo = kubernetesDeploymentRepo;
    }

    @ShellAuthentication(resource = "/kubernetes/deployment/sidecar/copy")
    @ShellMethod(key = COMMAND_DEPLOYMENT_SIDECAR_COPY, value = "Copy deployment sidecar")
    public void deploymentSidecarCopy(
            @ShellOption(help = "{EdsKubernetesInstanceName}:{Namespace}:{Deployment}:{Container}", defaultValue = "") String from,
            @ShellOption(help = "{EdsKubernetesInstanceName}:{Namespace}:{Deployment}", defaultValue = "") String to) {
        // Strategy=Replace
        KubernetesDeploymentSidecarParam.CopyDeploymentSidecar copyParam = KubernetesDeploymentSidecarParam.CopyDeploymentSidecar.parse(
                from, to);
        if (copyParam == null) {
            return;
        }
        // source
        EdsInstance sourceInstance = edsInstanceService.getByName(copyParam.getFromInstanceName());
        if (sourceInstance == null) {
            helper.printError(
                    StringFormatter.format("Eds instance {} does not exist.", copyParam.getFromInstanceName()));
            return;
        }
        if (!EdsInstanceTypeEnum.KUBERNETES.name()
                .equals(sourceInstance.getEdsType())) {
            helper.printError("Eds instance incorrect type.");
            return;
        }
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> fromInstanceProviderHolder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                sourceInstance.getId(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        Deployment sourceDeployment = kubernetesDeploymentRepo.get(fromInstanceProviderHolder.getInstance()
                .getEdsConfigModel(), copyParam.getFromNamespace(), copyParam.getFromDeploymentName());
        Optional<Container> optionalFromContainer = Optional.ofNullable(sourceDeployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getSpec)
                .map(PodSpec::getContainers)
                .flatMap(containers -> containers.stream()
                        .filter(c -> c.getName()
                                .equals(copyParam.getFromContainerName()))
                        .findFirst());
        if (optionalFromContainer.isEmpty()) {
            helper.printError("Source container not found.");
            return;
        }
        // target
        EdsInstance toInstance = edsInstanceService.getByName(copyParam.getToInstanceName());
        if (toInstance == null) {
            helper.printError(StringFormatter.format("Eds instance {} does not exist.", copyParam.getToInstanceName()));
            return;
        }
        if (!EdsInstanceTypeEnum.KUBERNETES.name()
                .equals(toInstance.getEdsType())) {
            helper.printError("Eds instance incorrect type.");
            return;
        }
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> toInstanceProviderHolder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                toInstance.getId(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        Deployment targetDeployment = kubernetesDeploymentRepo.get(toInstanceProviderHolder.getInstance()
                .getEdsConfigModel(), copyParam.getToNamespace(), copyParam.getToDeploymentName());
        if (targetDeployment == null) {
            helper.printError("Target deployment not found.");
            return;
        }
        // 替换策略=替换，容器已存在
        targetDeployment.getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .removeIf(c -> c.getName()
                        .equals(copyParam.getFromContainerName()));
        targetDeployment.getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .add(optionalFromContainer.get());
        kubernetesDeploymentRepo.update(toInstanceProviderHolder.getInstance()
                .getEdsConfigModel(), targetDeployment);
        helper.print("Sidecar container copied successfully.", PromptColor.GREEN);
    }

    @ShellAuthentication(resource = "/kubernetes/deployment/sidecar/remove")
    @ShellMethod(key = COMMAND_DEPLOYMENT_SIDECAR_REMOVE, value = "Remove deployment sidecar")
    public void deploymentSidecarRemove(
            @ShellOption(help = "{EdsKubernetesInstanceName}:{Namespace}:{Deployment}:{Container}", defaultValue = "") String target) {
        KubernetesDeploymentSidecarParam.RemoveDeploymentSidecar removeParam = KubernetesDeploymentSidecarParam.RemoveDeploymentSidecar.parse(
                target);
        if (removeParam == null) {
            return;
        }
        // source
        EdsInstance instance = edsInstanceService.getByName(removeParam.getInstanceName());
        if (instance == null) {
            helper.printError(StringFormatter.format("Eds instance {} does not exist.", removeParam.getInstanceName()));
            return;
        }
        if (!EdsInstanceTypeEnum.KUBERNETES.name()
                .equals(instance.getEdsType())) {
            helper.printError("Eds instance incorrect type.");
            return;
        }
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> instanceProviderHolder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                instance.getId(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        Deployment deployment = kubernetesDeploymentRepo.get(instanceProviderHolder.getInstance()
                .getEdsConfigModel(), removeParam.getNamespace(), removeParam.getDeploymentName());
        Optional<Container> optionalContainer = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getSpec)
                .map(PodSpec::getContainers)
                .flatMap(containers -> containers.stream()
                        .filter(c -> c.getName()
                                .equals(removeParam.getContainerName()))
                        .findFirst());
        if (optionalContainer.isEmpty()) {
            helper.printError("Remove container not found.");
            return;
        }
        deployment.getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .remove(optionalContainer.get());
        kubernetesDeploymentRepo.update(instanceProviderHolder.getInstance()
                .getEdsConfigModel(), deployment);
        helper.print("Sidecar container was successfully removed.", PromptColor.GREEN);
    }

}