package com.baiyi.cratos.facade.fin.impl;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AppFinOpsModel;
import com.baiyi.cratos.domain.param.http.finops.FinOpsParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.finops.FinOpsVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.fin.FinOpsFacade;
import com.baiyi.cratos.service.*;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.SHORT_TERM;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_REPLICAS;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/16 16:07
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FinOpsFacadeImpl implements FinOpsFacade {

    private final BusinessTagService businessTagService;
    private final BusinessTagFacade businessTagFacade;
    private final TagService tagService;
    private final ApplicationService applicationService;
    private final ApplicationResourceService applicationResourceService;
    private final EdsAssetIndexService assetIndexService;

    private static final String PROD_NAMESPACE = "prod";
    public final static String[] COST_TABLE_FIELD_NAME = {"Business", "Share", "%"};
    public final static String[] COST_DETAILS_TABLE_FIELD_NAME = {"Business", "Application Name", "Resources", "Share", "%"};

    @Override
    public FinOpsVO.AppCost queryAppCost(FinOpsParam.QueryAppCost queryAppCost) {
        if (!CollectionUtils.isEmpty(queryAppCost.getAllocationCategories())) {
            List<String> fieldNames = new ArrayList<>(Arrays.asList(COST_TABLE_FIELD_NAME));
            queryAppCost.getAllocationCategories()
                    .forEach(category -> {
                        String name = StringUtils.hasText(category.getCurrencyCode()) ? StringFormatter.arrayFormat(
                                "{} ({} {})", category.getName(), category.getCurrencyCode(),
                                String.format("%,d", category.getAmount())) : category.getName();
                        fieldNames.add(name);
                    });
            return calculateCost(queryAppCost.getAllocationCategories(), fieldNames.toArray(new String[0]));
        }
        return calculateCost(List.of(), COST_TABLE_FIELD_NAME);
    }

    private FinOpsVO.AppCost calculateCost(List<FinOpsParam.AllocationCategory> allocationCategories,
                                           String[] fieldNames) {
        Map<String, List<AppFinOpsModel.AppCost>> finAppCostMap = ((FinOpsFacadeImpl) AopContext.currentProxy()).calculateFinAppCostMap();
        PrettyTable costTable = PrettyTable.fieldNames(fieldNames);
        int totalReplicas = finAppCostMap.values()
                .stream()
                .flatMap(List::stream)
                .flatMap(appCost -> appCost.getAppResources()
                        .stream())
                .mapToInt(AppFinOpsModel.AppResource::getReplicas)
                .sum();
        if (totalReplicas == 0) {
            return FinOpsVO.AppCost.builder()
                    .costTable(costTable.toString())
                    .costDetailsTable(getCostDetailsTable(finAppCostMap))
                    .build();
        }
        finAppCostMap.forEach((business, appCosts) -> {
            // 按 totalReplicas 降序排序
            List<AppFinOpsModel.AppCost> sortedAppCosts = appCosts.stream()
                    .sorted(Comparator.comparingInt(AppFinOpsModel.AppCost::getTotalReplicas)
                            .reversed())
                    .toList();
            int replicas = sortedAppCosts.stream()
                    .mapToInt(AppFinOpsModel.AppCost::getTotalReplicas)
                    .sum();
            double ratioValue = replicas * 100.0 / totalReplicas;
            String ratio = String.format("%.2f", ratioValue);
            Object[] rowData = new Object[fieldNames.length];
            rowData[0] = business;
            rowData[1] = replicas + "/" + totalReplicas;
            rowData[2] = ratio;
            for (int i = 0; i < allocationCategories.size(); i++) {
                double amount = allocationCategories.get(i)
                        .getAmount();
                rowData[3 + i] = ratioValue == 0 ? "0.00" : String.format("%,.2f", amount * (ratioValue / 100.0));
            }
            costTable.addRow(rowData);
        });
        costTable.sortTable("%");
        return FinOpsVO.AppCost.builder()
                .costTable(costTable.toString())
                .costDetailsTable(getCostDetailsTable(finAppCostMap))
                .build();
    }

    @Cacheable(cacheNames = SHORT_TERM, key = "'FINOPS:APP:COST:MAP'", unless = "#result == null")
    public Map<String, List<AppFinOpsModel.AppCost>> calculateFinAppCostMap() {
        Tag finTag = tagService.getByTagKey(SysTagKeys.BUSINESS);
        if (Objects.isNull(finTag)) {
            return Map.of();
        }
        List<BusinessTag> businessTags = businessTagService.queryByBusinessTypeAndTagId(
                BusinessTypeEnum.APPLICATION.name(), finTag.getId());
        if (CollectionUtils.isEmpty(businessTags)) {
            return Map.of();
        }
        BusinessTagParam.QueryByTag queryByValue = BusinessTagParam.QueryByTag.builder()
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .tagId(finTag.getId())
                .build();
        List<String> finValues = businessTagFacade.queryBusinessTagValue(queryByValue);
        Map<String, List<AppFinOpsModel.AppCost>> finAppCostMap = Maps.newHashMap();
        businessTags.forEach(businessTag -> {
            Application application = applicationService.getById(businessTag.getBusinessId());
            if (Objects.isNull(application)) {
                log.warn("Application not found for business tag: {}", businessTag.getId());
                return;
            }
            List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                    application.getName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), PROD_NAMESPACE);
            if (CollectionUtils.isEmpty(resources)) {
                log.warn("No resources found for application: {}", application.getName());
                return;
            }
            AppFinOpsModel.AppCost appCost = AppFinOpsModel.AppCost.builder()
                    .applicationName(application.getName())
                    .build();
            resources.forEach(resource -> {
                EdsAssetIndex index = assetIndexService.getByAssetIdAndName(resource.getBusinessId(),
                        KUBERNETES_REPLICAS);
                if (Objects.nonNull(index)) {
                    AppFinOpsModel.AppResource appResource = AppFinOpsModel.AppResource.builder()
                            .resourceName(resource.getName())
                            .resourceType(resource.getResourceType())
                            .replicas(Integer.parseInt(index.getValue()))
                            .build();
                    appCost.addAppResource(appResource);
                }
            });
            finAppCostMap.computeIfAbsent(businessTag.getTagValue(), k -> Lists.newArrayList())
                    .add(appCost);
        });
        return finAppCostMap;
    }

    private String getCostDetailsTable(Map<String, List<AppFinOpsModel.AppCost>> finAppCostMap) {
        PrettyTable costTable = PrettyTable.fieldNames(COST_DETAILS_TABLE_FIELD_NAME);
        int totalReplicas = finAppCostMap.values()
                .stream()
                .flatMap(List::stream)
                .mapToInt(appCost -> appCost.getAppResources()
                        .stream()
                        .mapToInt(AppFinOpsModel.AppResource::getReplicas)
                        .sum())
                .sum();
        finAppCostMap.forEach((business, appCosts) -> {
            for (AppFinOpsModel.AppCost appCost : appCosts) {
                StringBuilder resources = new StringBuilder();
                appCost.getAppResources()
                        .forEach(resource -> {
                            resources.append(resource.getResourceName())
                                    .append(" (")
                                    .append(resource.getReplicas())
                                    .append(") ");
                        });
                String ratio = String.format("%.2f", appCost.getTotalReplicas() * 100.0 / totalReplicas);
                costTable.addRow(business, appCost.getApplicationName(), resources.toString(),
                        appCost.getTotalReplicas() + "/" + totalReplicas, ratio);
            }
        });
        return costTable.toString();
    }

}
