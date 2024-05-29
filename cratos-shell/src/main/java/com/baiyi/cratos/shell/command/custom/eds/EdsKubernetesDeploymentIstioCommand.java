package com.baiyi.cratos.shell.command.custom.eds;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.shell.command.custom.eds.EdsCloudComputerListCommand.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/21 上午9:58
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds Kubernetes Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsKubernetesDeploymentIstioCommand extends AbstractCommand {

    public static final String GROUP = "kubernetes-deployment";

    private static final String COMMAND_ISTIO_LIST = GROUP + "-istio-list";

    private final EdsAssetService edsAssetService;

    private final EdsInstanceService edsInstanceService;

    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    public static final String SIDECAR_ISTIO_IO_INJECT = "sidecar.istio.io/inject";

    public final static String[] TABLE_FIELD_NAME = {"Eds Instance", "Namespace", "Deployment", "Istio"};

    public EdsKubernetesDeploymentIstioCommand(SshShellHelper helper, SshShellProperties properties,
                                               EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                               EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(helper, properties, properties.getCommands()
                .getAsset());
        this.edsInstanceService = edsInstanceService;
        this.edsAssetService = edsAssetService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
    }

    @ShellMethod(key = COMMAND_ISTIO_LIST, value = "List istio configuration")
    public void istioConfigurationList(
            @ShellOption(help = "Eds Kubernetes Instance Name", defaultValue = "") String edsInstanceName) {
        PrettyTable istioTable = PrettyTable.fieldNames(TABLE_FIELD_NAME);
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
        // 不分页
        EdsInstanceParam.AssetPageQuery pageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .instanceId(edsInstance.getId())
                .assetType(EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
                .page(1)
                .length(1024)
                .build();
        DataTable<EdsAsset> table = edsAssetService.queryEdsInstanceAssetPage(pageQuery);
        if (CollectionUtils.isEmpty(table.getData())) {
            helper.printInfo("No available assets found.");
        }

        for (EdsAsset asset : table.getData()) {
            Deployment deployment = edsInstanceProviderHolder.getProvider()
                    .assetLoadAs(asset.getOriginalModel());

            Map<String, String> labels = Optional.ofNullable(deployment)
                    .map(Deployment::getSpec)
                    .map(DeploymentSpec::getTemplate)
                    .map(PodTemplateSpec::getMetadata)
                    .map(ObjectMeta::getLabels)
                    .orElse(Maps.newHashMap());
            boolean enabled = false;
            if (labels.containsKey(SIDECAR_ISTIO_IO_INJECT)) {
                enabled = "true".equals(labels.get(SIDECAR_ISTIO_IO_INJECT));
            }
            final String namespace = Optional.ofNullable(deployment)
                    .map(Deployment::getMetadata)
                    .map(ObjectMeta::getNamespace)
                    .orElse("");
            final String name = Optional.ofNullable(deployment)
                    .map(Deployment::getMetadata)
                    .map(ObjectMeta::getName)
                    .orElse("");
            final String istioEnabled = toIstioEnabled(enabled);
            istioTable.addRow(edsInstanceName, namespace, name, istioEnabled);
        }
        helper.print(istioTable.toString());
    }

    private String toIstioEnabled(boolean enabled) {
        return enabled ? helper.getColored("Yes", PromptColor.GREEN) : helper.getColored("No", PromptColor.WHITE);
    }

}
