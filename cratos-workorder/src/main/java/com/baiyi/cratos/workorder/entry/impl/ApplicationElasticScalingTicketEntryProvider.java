package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.util.GroupingUtils;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.domain.model.ApplicationReplicasModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.report.ListAppGroup;
import com.baiyi.cratos.eds.report.model.AppGroupSpec;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ApplicationElasticScalingTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.ElasticScalingTypes;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_REPLICAS;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/9 13:34
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@BusinessType(type = BusinessTypeEnum.APPLICATION)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_ELASTIC_SCALING)
public class ApplicationElasticScalingTicketEntryProvider extends BaseTicketEntryProvider<ApplicationReplicasModel.ApplicationConfigurationChange, WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry> {

    private final ApplicationResourceService applicationResourceService;
    private final ListAppGroup listAppGroup;
    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService edsAssetIndexService;
    private final WorkOrderTicketEntryService workOrderTicketEntryService;

    public ApplicationElasticScalingTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                        WorkOrderTicketService workOrderTicketService,
                                                        WorkOrderService workOrderService,
                                                        ApplicationResourceService applicationResourceService,
                                                        ListAppGroup listAppGroup, EdsAssetService edsAssetService,
                                                        EdsAssetIndexService edsAssetIndexService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.applicationResourceService = applicationResourceService;
        this.listAppGroup = listAppGroup;
        this.edsAssetService = edsAssetService;
        this.edsAssetIndexService = edsAssetIndexService;
        this.workOrderTicketEntryService = workOrderTicketEntryService;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Application Name | Namespace | Current Replicas | Expected Replicas | Scaling Type |
                | --- | --- | --- | --- | --- |
                """;
    }

    private static final String ROW_TPL = "| {} | {} | {} | {} | {} |";

    /**
     * @param entry
     * @return
     */
    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        ApplicationReplicasModel.ApplicationConfigurationChange configurationChange = loadAs(entry);
        return StringFormatter.arrayFormat(ROW_TPL, entry.getName(), entry.getNamespace(),
                configurationChange.getConfig()
                        .getCurrentReplicas(), configurationChange.getConfig()
                        .getExpectedReplicas(), configurationChange.getConfig()
                        .getElasticScalingType());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        ApplicationReplicasModel.ApplicationConfigurationChange configurationChange = loadAs(entry);
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Application elastic scaling")
                .build();
    }

    @Override
    public WorkOrderTicketEntry addEntry(WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry param) {
        WorkOrderTicketEntry workOrderTicketEntry = super.addEntry(param);
        addApplicationDeploymentAssets(param);
        TicketEntryProvider<ApplicationDeploymentModel.DeploymentScale, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry> ticketEntryProvider = (TicketEntryProvider<ApplicationDeploymentModel.DeploymentScale, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_ELASTIC_SCALING.name(), BusinessTypeEnum.EDS_ASSET.name());
        int currentReplicas = workOrderTicketEntryService.queryTicketEntries(workOrderTicketEntry.getTicketId(),
                        BusinessTypeEnum.EDS_ASSET.name())
                .stream()
                .map(e -> {
                    ApplicationDeploymentModel.DeploymentScale deploymentScale = Objects.requireNonNull(
                                    ticketEntryProvider)
                            .loadAs(e);
                    return deploymentScale.getCurrentReplicas();
                })
                .mapToInt(Integer::intValue)
                .sum();
        ApplicationReplicasModel.ApplicationConfigurationChange applicationConfigurationChange = loadAs(
                workOrderTicketEntry);
        applicationConfigurationChange.getConfig()
                .setCurrentReplicas(currentReplicas);
        int expectedReplicas = applicationConfigurationChange.getConfig()
                .getExpectedReplicas();
        ElasticScalingTypes elasticScalingType = getElasticScalingType(currentReplicas, expectedReplicas);
        applicationConfigurationChange.getConfig()
                .setElasticScalingType(elasticScalingType.name());
        workOrderTicketEntry.setContent(YamlUtils.dump(applicationConfigurationChange));
        workOrderTicketEntryService.save(workOrderTicketEntry);
        return workOrderTicketEntry;
    }

    private ElasticScalingTypes getElasticScalingType(int currentReplicas, int expectedReplicas) {
        if (currentReplicas == expectedReplicas) {
            return ElasticScalingTypes.REBALANCING;
        }
        return expectedReplicas > currentReplicas ? ElasticScalingTypes.EXPANSION : ElasticScalingTypes.REDUCTION;
    }

    private void addApplicationDeploymentAssets(WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry param) {
        // 查询应用所有绑定的Deployments
        String applicationName = param.getDetail()
                .getApplication()
                .getName();
        List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(applicationName,
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getDetail()
                        .getNamespace());
        if (CollectionUtils.isEmpty(resources)) {
            WorkOrderTicketException.runtime("No application deployment resources found.");
        }
        Map<String, AppGroupSpec.GroupSpec> groupingMap = listAppGroup.getGroupMap(applicationName, true);
        if (groupingMap.size() != 1) {
            WorkOrderTicketException.runtime("There is only one application group with name {}.", applicationName);
        }
        AppGroupSpec.GroupSpec groupSpec = groupingMap.values()
                .stream()
                .findFirst()
                .get();
        int total = groupSpec.countTotalReplicas();
        allocateReplicas(param.getDetail()
                .getConfig()
                .getExpectedReplicas(), groupSpec);
        // 下一步要做的
        int compensateReplicas = 0;
        // G4
        compensateReplicas = compute(groupSpec.getG4(), resources, param.getTicketId(), param.getDetail()
                .getNamespace(), compensateReplicas);
        compensateReplicas = compute(groupSpec.getG3(), resources, param.getTicketId(), param.getDetail()
                .getNamespace(), compensateReplicas);
        compensateReplicas = compute(groupSpec.getG2(), resources, param.getTicketId(), param.getDetail()
                .getNamespace(), compensateReplicas);
        compute(groupSpec.getG1(), resources, param.getTicketId(), param.getDetail()
                .getNamespace(), compensateReplicas);
    }

    private int compute(AppGroupSpec.Group group, List<ApplicationResource> resources, int ticketId, String namespace,
                        int compensateReplicas) {
        if (StringUtils.hasText(group.getName())) {
            Optional<ApplicationResource> optionalApplicationResource = resources.stream()
                    .filter(resource -> resource.getName()
                            .equals(group.getName()))
                    .findFirst();
            if (optionalApplicationResource.isPresent()) {
                EdsAssetVO.Asset asset = getDeploymentAsset(optionalApplicationResource.get());
                EdsAssetIndex replicasIndex = edsAssetIndexService.getByAssetIdAndName(asset.getId(),
                        KUBERNETES_REPLICAS);
                int expectedReplicas = group.getExpectedReplicas() + compensateReplicas;
                ApplicationDeploymentModel.DeploymentScale detail = ApplicationDeploymentModel.DeploymentScale.builder()
                        .deployment(asset)
                        .namespace(namespace)
                        .currentReplicas(replicasIndex != null ? Integer.parseInt(replicasIndex.getValue()) : -1)
                        .expectedReplicas(expectedReplicas)
                        .build();
                WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry addDeploymentParam = WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry.builder()
                        .ticketId(ticketId)
                        .detail(detail)
                        .build();
                addDeploymentParam(addDeploymentParam);
                return 0;
            } else {
                return compensateReplicas + group.getExpectedReplicas();
            }
        } else {
            return compensateReplicas + group.getExpectedReplicas();
        }
    }

    private EdsAssetVO.Asset getDeploymentAsset(ApplicationResource resource) {
        return BeanCopierUtils.copyProperties(edsAssetService.getById(resource.getBusinessId()), EdsAssetVO.Asset.class);
    }

    private void addDeploymentParam(WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry param) {
        TicketEntryProvider<ApplicationDeploymentModel.DeploymentScale, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry> applicationDeploymentScaleTicketEntryProvider = (TicketEntryProvider<ApplicationDeploymentModel.DeploymentScale, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry>) TicketEntryProviderFactory.getProvider(
                getKey(), BusinessTypeEnum.EDS_ASSET.name());
        if (applicationDeploymentScaleTicketEntryProvider != null) {
            applicationDeploymentScaleTicketEntryProvider.addEntry(param);
        }
    }

    private void allocateReplicas(int total, AppGroupSpec.GroupSpec groupingSpecifications) {
        List<Integer> groups = GroupingUtils.getGroups(total);
        groupingSpecifications.setG1(
                setGroupExpectedReplicas(groupingSpecifications.getG1(), !groups.isEmpty() ? groups.getFirst() : 0));
        groupingSpecifications.setG2(
                setGroupExpectedReplicas(groupingSpecifications.getG2(), groups.size() > 1 ? groups.get(1) : 0));
        groupingSpecifications.setG3(
                setGroupExpectedReplicas(groupingSpecifications.getG3(), groups.size() > 2 ? groups.get(2) : 0));
        groupingSpecifications.setG4(
                setGroupExpectedReplicas(groupingSpecifications.getG4(), groups.size() > 3 ? groups.get(3) : 0));
    }

    private AppGroupSpec.Group setGroupExpectedReplicas(AppGroupSpec.Group group, int expectedReplicas) {
        if (Objects.isNull(group)) {
            return AppGroupSpec.Group.builder()
                    .expectedReplicas(expectedReplicas)
                    .build();
        }
        group.setExpectedReplicas(expectedReplicas);
        return group;
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                ApplicationReplicasModel.ApplicationConfigurationChange applicationConfigurationChange) throws WorkOrderTicketException {
        // 只读条目无需处理
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry param) {
        return ApplicationElasticScalingTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
