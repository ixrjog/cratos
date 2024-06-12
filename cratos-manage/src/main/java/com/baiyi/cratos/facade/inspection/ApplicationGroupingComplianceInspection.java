package com.baiyi.cratos.facade.inspection;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.BeetlUtil;
import com.baiyi.cratos.common.util.GroupingUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.EdsInstanceHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkRobotService;
import com.baiyi.cratos.eds.report.ListAppGroup;
import com.baiyi.cratos.eds.report.model.AppGroupSpec;
import com.baiyi.cratos.facade.inspection.model.ApplicationGroupingModel;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.domain.constant.Global.APP_NAME;
import static com.baiyi.cratos.eds.kubernetes.provider.EdsKubernetesDeploymentAssetProvider.REPLICAS;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/12 上午10:22
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class ApplicationGroupingComplianceInspection extends BaseInspection {

    private final EdsAssetService edsAssetService;

    private final EdsAssetIndexService edsAssetIndexService;

    public static final String APPLICATION_GROUPING_COMPLIANCE_INSPECTION_NOTIFICATION = "APPLICATION_GROUPING_COMPLIANCE_INSPECTION_NOTIFICATION";

    private static final String MSG_TPL = "{}[{}/{}][{}/{}][{}/{}][{}/{}]<{}>";

    public ApplicationGroupingComplianceInspection(NotificationTemplateService notificationTemplateService,
                                                   DingtalkRobotService dingtalkRobotService,
                                                   EdsInstanceHelper edsInstanceHelper,
                                                   EdsConfigService edsConfigService, ListAppGroup listAppGroup,
                                                   EdsAssetService edsAssetService,
                                                   EdsAssetIndexService edsAssetIndexService) {
        super(notificationTemplateService, dingtalkRobotService, edsInstanceHelper, edsConfigService);
        this.edsAssetService = edsAssetService;
        this.edsAssetIndexService = edsAssetIndexService;
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                APPLICATION_GROUPING_COMPLIANCE_INSPECTION_NOTIFICATION);
        Map<String, AppGroupSpec.GroupSpec> groupMap = getGroupMap();
        List<ApplicationGroupingModel.ApplicationGrouping> applicationGroupingList = Lists.newArrayList();
        groupMap.keySet()
                .forEach(k -> {
                    final AppGroupSpec.GroupSpec e = groupMap.get(k);
                    final String appName = e.getAppName();
                    final int total = e.countTotalReplicas();
                    final String canaryName = Optional.ofNullable(e.getCanary())
                            .map(AppGroupSpec.Group::getName)
                            .orElse("");
                    final String canary = StringUtils.hasText(canaryName) ? "[canary]" : "";

                    final int g1 = Optional.ofNullable(e.getG1())
                            .map(AppGroupSpec.Group::getReplicas)
                            .orElse(0);

                    final int g2 = Optional.ofNullable(e.getG2())
                            .map(AppGroupSpec.Group::getReplicas)
                            .orElse(0);

                    final int g3 = Optional.ofNullable(e.getG3())
                            .map(AppGroupSpec.Group::getReplicas)
                            .orElse(0);
                    final int g4 = Optional.ofNullable(e.getG4())
                            .map(AppGroupSpec.Group::getReplicas)
                            .orElse(0);

                    List<Integer> groups = Lists.newArrayList();
                    GroupingUtil.grouping(total, groups);
                    groups = groups.stream()
                            .sorted(Comparator.comparingInt(Integer::intValue))
                            .toList();
                    final int g1C = !groups.isEmpty() ? groups.get(0) : 0;
                    final int g2C = groups.size() > 1 ? groups.get(1) : 0;
                    final int g3C = groups.size() > 2 ? groups.get(2) : 0;
                    final int g4C = groups.size() > 3 ? groups.get(3) : 0;
                    if (g1 != g1C || g2 != g2C || g3 != g3C || g4 != g4C) {
                        ApplicationGroupingModel.ApplicationGrouping applicationGrouping = ApplicationGroupingModel.ApplicationGrouping.builder()
                                .appName(appName)
                                .grouping(
                                        StringFormatter.arrayFormat(MSG_TPL, canary, g1, g1C, g2, g2C, g3, g3C, g4, g4C,
                                                total))
                                .build();
                        applicationGroupingList.add(applicationGrouping);
                    }
                });
        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put("applicationGroupings", applicationGroupingList)
                .build());
    }

    public Map<String, AppGroupSpec.GroupSpec> getGroupMap() {
        List<EdsAssetIndex> appNameIndices = edsAssetIndexService.queryIndexByName(APP_NAME);
        Map<String, AppGroupSpec.GroupSpec> groupingMap = Maps.newHashMap();
        Map<Integer, EdsAsset> assetMap = Maps.newHashMap();

        for (EdsAssetIndex appNameIndex : appNameIndices) {
            // 过滤掉 h5应用
            if (appNameIndex.getValue()
                    .endsWith("-h5")) {
                continue;
            }
            // 获取资产信息
            EdsAsset edsAsset = assetMap.containsKey(appNameIndex.getAssetId()) ? assetMap.get(
                    appNameIndex.getAssetId()) : edsAssetService.getById(appNameIndex.getAssetId());
            if (edsAsset == null) {
                continue;
            }
            assetMap.put(appNameIndex.getAssetId(), edsAsset);

            // 获取资产的所有索引
            List<EdsAssetIndex> assetIndices = edsAssetIndexService.queryIndexByAssetId(appNameIndex.getAssetId());

            // 从索引中查询 env & replicas;
            Optional<EdsAssetIndex> envIndexOptional = assetIndices.stream()
                    .filter(e -> e.getName()
                            .equals("env"))
                    .findFirst();
            if (envIndexOptional.isEmpty()) {
                continue;
            }
            // 只查看生产环境
            if (!envIndexOptional.get()
                    .getValue()
                    .equals("prod")) {
                continue;
            }

            Optional<EdsAssetIndex> replicasIndexOptional = assetIndices.stream()
                    .filter(e -> e.getName()
                            .equals(REPLICAS))
                    .findFirst();
            if (replicasIndexOptional.isEmpty()) {
                continue;
            }

            AppGroupSpec.GroupSpec groupSpecifications;
            if (groupingMap.containsKey(appNameIndex.getValue())) {
                groupSpecifications = groupingMap.get(appNameIndex.getValue());
            } else {
                groupSpecifications = AppGroupSpec.GroupSpec.builder()
                        .appName(appNameIndex.getValue())
                        .env(envIndexOptional.get()
                                .getValue())
                        .build();
            }
            // 通过资产名称来设置分组副本数
            AppGroupSpec.Group group = AppGroupSpec.Group.builder()
                    .name(edsAsset.getName())
                    .replicas(Integer.parseInt(replicasIndexOptional.get()
                            .getValue()))
                    .build();

            // 去掉环境表情
            String newGroupName = StringFormatter.eraseLastStr(edsAsset.getName(), "-" + envIndexOptional.get()
                    .getValue());
            // 分组名称 = 应用名称
            if (appNameIndex.getValue()
                    .equals(newGroupName)) {
                groupSpecifications.setG1(group);
                groupingMap.put(appNameIndex.getValue(), groupSpecifications);
                continue;
            }

            if (newGroupName.endsWith("-canary")) {
                group.setReplicas(1);
                groupSpecifications.setCanary(group);
                groupingMap.put(appNameIndex.getValue(), groupSpecifications);
                continue;
            }

            if (newGroupName.endsWith("-1")) {
                groupSpecifications.setG1(group);
                groupingMap.put(appNameIndex.getValue(), groupSpecifications);
                continue;
            }

            if (newGroupName.endsWith("-2")) {
                groupSpecifications.setG2(group);
                groupingMap.put(appNameIndex.getValue(), groupSpecifications);
                continue;
            }

            if (newGroupName.endsWith("-3")) {
                groupSpecifications.setG3(group);
                groupingMap.put(appNameIndex.getValue(), groupSpecifications);
            }

            if (newGroupName.endsWith("-4")) {
                groupSpecifications.setG4(group);
                groupingMap.put(appNameIndex.getValue(), groupSpecifications);
            }
        }
        return groupingMap;
    }

}

