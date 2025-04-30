package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.util.GroupingUtils;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationReplicasModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.report.ListAppGroup;
import com.baiyi.cratos.eds.report.model.AppGroupSpec;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ApplicationElasticScalingTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/9 13:34
 * &#064;Version 1.0
 */
@BusinessType(type = BusinessTypeEnum.APPLICATION)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_ELASTIC_SCALING)
public class ApplicationElasticScalingTicketEntryProvider extends BaseTicketEntryProvider<ApplicationReplicasModel.ApplicationConfigurationChange, WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry> {

    private final ApplicationResourceService applicationResourceService;
    private final ListAppGroup listAppGroup;

    public ApplicationElasticScalingTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                        WorkOrderTicketService workOrderTicketService,
                                                        WorkOrderService workOrderService,
                                                        ApplicationResourceService applicationResourceService,
                                                        ListAppGroup listAppGroup) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.applicationResourceService = applicationResourceService;
        this.listAppGroup = listAppGroup;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Application Name | Namespace | Current Replicas | Expected Replicas |
                | --- | --- | --- | --- |
                """;
    }

    private static final String ROW_TPL = "| {} | {} | {} | {} |";

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
                        .getExpectedReplicas());
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
        addApplicationDeploymentAssets(param);
        return super.addEntry(param);
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
            WorkOrderTicketException.runtime(
                    "There is only one application group with name {}.", applicationName);
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
        if(StringUtils.hasText(groupSpec.getG4().getName())) {

            resources.stream().filter(resource -> resource.getName().equals(groupSpec.getG4().getName())).findFirst().ifPresent(resource -> {

            });

        }else{

        }


    }

    private void allocateReplicas(int total, AppGroupSpec.GroupSpec groupingSpecifications) {
        List<Integer> groups = GroupingUtils.getGroups(total);
        groupingSpecifications.setG1(setGroupExpectedReplicas(groupingSpecifications.getG1(),!groups.isEmpty() ? groups.getFirst() : 0));
        groupingSpecifications.setG2(setGroupExpectedReplicas(groupingSpecifications.getG2(),groups.size() > 1 ? groups.get(1) : 0));
        groupingSpecifications.setG3(setGroupExpectedReplicas(groupingSpecifications.getG3(),groups.size() > 2 ? groups.get(2) : 0));
        groupingSpecifications.setG4(setGroupExpectedReplicas(groupingSpecifications.getG4(),groups.size() > 3 ? groups.get(3) : 0));
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
        // TODO: Implement the logic to process the entry
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry param) {
        return ApplicationElasticScalingTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
