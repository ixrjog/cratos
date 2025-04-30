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
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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


        Map<String, AppGroupSpec.GroupSpec> groupingMap = listAppGroup.getGroupMap(applicationName, false);

        groupingMap.keySet()
                .forEach(k -> {
                    final AppGroupSpec.GroupSpec groupSpec = groupingMap.get(k);
                    int total = groupSpec.countTotalReplicas();
                    final String rowCanary = Optional.ofNullable(groupSpec.getCanary())
                            .map(AppGroupSpec.Group::getName)
                            .orElse("");
                    final String g1Name = groupSpec.getG1() != null ? groupSpec.getG1()
                            .getName() : "";
                    int g1Replicas = groupSpec.getG1() != null ? groupSpec.getG1()
                            .getReplicas() : 0;
                    final String g2Name = groupSpec.getG2() != null ? groupSpec.getG2()
                            .getName() : "";
                    int g2Replicas = groupSpec.getG2() != null ? groupSpec.getG2()
                            .getReplicas() : 0;

                    final String g3Name = groupSpec.getG3() != null ? groupSpec.getG3()
                            .getName() : "";
                    int g3Replicas = groupSpec.getG3() != null ? groupSpec.getG3()
                            .getReplicas() : 0;

                    final String g4Name = groupSpec.getG4() != null ? groupSpec.getG4()
                            .getName() : "";
                    int g4Replicas = groupSpec.getG4() != null ? groupSpec.getG4()
                            .getReplicas() : 0;

                    groupSpec.getSpecifications().getIsCompliant();

                    List<Integer> groups = Lists.newArrayList();
                    GroupingUtils.grouping(total, groups);


//                        table.addRow(rowAppName, total, rowCanary, rowG1, rowG1Replicas, rowG2, rowG2Replicas, rowG3,
//                                rowG3Replicas, rowG4, rowG4Replicas, getSpecifications(e));


                });

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
