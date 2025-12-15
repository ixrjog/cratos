package com.baiyi.cratos.shell.command.custom.kubernetes;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.shell.command.custom.kubernetes.EdsKubernetesDeploymentResourceCommand.GROUP;


/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/21 下午5:29
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds Kubernetes Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsKubernetesDeploymentResourceCommand extends AbstractCommand {

    public static final String GROUP = "kubernetes-deployment";
    private static final String COMMAND_DEPLOYMENT_RES_LIST = GROUP + "-resource-list";
    private final EdsAssetService edsAssetService;
    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    public static final String SIDECAR_ISTIO_IO_INJECT = "sidecar.istio.io/inject";
    public final static String[] TABLE_FIELD_NAME = {"Eds Instance", "Namespace", "Deployment", "Container", "Res Limits", "Res Requests"};

    public EdsKubernetesDeploymentResourceCommand(SshShellHelper helper, SshShellProperties properties,
                                                  EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                                  EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(helper, properties, properties.getCommands()
                .getAsset());
        this.edsInstanceService = edsInstanceService;
        this.edsAssetService = edsAssetService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
    }

    @ShellMethod(key = COMMAND_DEPLOYMENT_RES_LIST, value = "List deployment resource")
    public void deploymentList(
            @ShellOption(help = "Eds Kubernetes Instance Name", defaultValue = "") String edsInstanceName,
            @ShellOption(help = "Query Name", defaultValue = "") String queryName) {
        PrettyTable deploymentTable = PrettyTable.fieldNames(TABLE_FIELD_NAME);
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
        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> edsInstanceProviderHolder = (EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                edsInstance.getId(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        // 不分页
        EdsInstanceParam.AssetPageQuery pageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .instanceId(edsInstance.getId())
                .assetType(EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
                .queryName(org.springframework.util.StringUtils.hasText(queryName) ? queryName : null)
                .page(1)
                .length(1024)
                .build();
        DataTable<EdsAsset> table = edsAssetService.queryEdsInstanceAssetPage(pageQuery);
        if (CollectionUtils.isEmpty(table.getData())) {
            helper.printInfo("No available assets found.");
        }
        table.getData()
                .stream()
                .map(asset -> edsInstanceProviderHolder.getProvider()
                        .assetLoadAs(asset.getOriginalModel()))
                .forEach(deployment -> {
                    final String namespace = Optional.ofNullable(deployment)
                            .map(Deployment::getMetadata)
                            .map(ObjectMeta::getNamespace)
                            .orElse("");
                    final String name = Optional.ofNullable(deployment)
                            .map(Deployment::getMetadata)
                            .map(ObjectMeta::getName)
                            .orElse("");
                    Optional<Container> optionalContainer = KubeUtils.findAppContainerOf(deployment);
                    if (optionalContainer.isEmpty()) {
                        deploymentTable.addRow(edsInstanceName, namespace, name, "-", "-", "-");
                    } else {
                        Container container = optionalContainer.get();
                        String containerName = container.getName();
                        Map<String, Quantity> limits = container.getResources()
                                .getLimits();
                        Map<String, Quantity> requests = container.getResources()
                                .getRequests();
                        deploymentTable.addRow(edsInstanceName, namespace, name, containerName, toCpuMem(limits),
                                toCpuMem(requests));
                    }
                });
        helper.print(deploymentTable.toString());
    }

    private String toCpuMem(Map<String, Quantity> res) {
        String result = "";
        if (res.containsKey("cpu")) {
            result += "cpu:" + res.get("cpu")
                    .getAmount() + res.get("cpu")
                    .getFormat();
        }
        if (res.containsKey("memory")) {
            result += "/mem:" + res.get("memory")
                    .getAmount() + res.get("memory")
                    .getFormat();
        }
        return result;
    }

}
