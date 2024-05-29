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
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

import static com.baiyi.cratos.shell.command.custom.eds.EdsCloudComputerListCommand.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 下午5:05
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds Kubernetes Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsKubernetesDeploymentListCommand extends AbstractCommand {

    public static final String GROUP = "kubernetes-deployment";

    private static final String COMMAND_DEPLOYMENT_LIST = GROUP + "-list";

    private final EdsAssetService edsAssetService;

    private final EdsInstanceService edsInstanceService;

    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    protected static final int PAGE_FOOTER_SIZE = 6;

    public final static String[] TABLE_FIELD_NAME = {"ID", "Eds Instance", "Namespace", "Deployment", "Replicas", "Containers"};

    public EdsKubernetesDeploymentListCommand(SshShellHelper helper, SshShellProperties properties,
                                              EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                              EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(helper, properties, properties.getCommands()
                .getAsset());
        this.edsInstanceService = edsInstanceService;
        this.edsAssetService = edsAssetService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
    }

    @ShellMethod(key = COMMAND_DEPLOYMENT_LIST, value = "List deployment")
    public void deploymentList(
            @ShellOption(help = "Eds Kubernetes Instance Name", defaultValue = "") String edsInstanceName,
            @ShellOption(help = "Query Name", defaultValue = "") String queryName,
            @ShellOption(help = "Page", defaultValue = "1") int page) {
        PrettyTable deploymentTable = PrettyTable.fieldNames(TABLE_FIELD_NAME);
        EdsInstance uniqueKey = EdsInstance.builder()
                .instanceName(edsInstanceName)
                .build();
        int rows = helper.terminalSize()
                .getRows();
        int pageLength = rows - PAGE_FOOTER_SIZE;
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
        EdsInstanceParam.AssetPageQuery pageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .instanceId(edsInstance.getId())
                .assetType(EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
                .queryName(org.springframework.util.StringUtils.hasText(queryName) ? queryName : null)
                .page(page)
                .length(pageLength)
                .build();
        DataTable<EdsAsset> table = edsAssetService.queryEdsInstanceAssetPage(pageQuery);
        if (CollectionUtils.isEmpty(table.getData())) {
            helper.printInfo("No available assets found.");
        }
        int i = 1;
        for (EdsAsset asset : table.getData()) {
            Deployment deployment = edsInstanceProviderHolder.getProvider()
                    .assetLoadAs(asset.getOriginalModel());
            final String namespace = Optional.ofNullable(deployment)
                    .map(Deployment::getMetadata)
                    .map(ObjectMeta::getNamespace)
                    .orElse("");
            final String name = Optional.ofNullable(deployment)
                    .map(Deployment::getMetadata)
                    .map(ObjectMeta::getName)
                    .orElse("");
            final int replicas = Optional.ofNullable(deployment)
                    .map(Deployment::getSpec)
                    .map(DeploymentSpec::getReplicas)
                    .orElse(0);
            Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(deployment);
            final String containers = optionalContainer.isEmpty() ? "-" : optionalContainer.get()
                    .getName();

            deploymentTable.addRow(i, edsInstanceName, namespace, name, replicas, containers);
            i++;
        }
        helper.print(deploymentTable.toString());
    }

}