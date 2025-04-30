package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.GroupingUtils;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.eds.report.ListAppGroup;
import com.baiyi.cratos.eds.report.model.AppGroupSpec;
import com.baiyi.cratos.facade.inspection.base.BaseInspection;
import com.baiyi.cratos.facade.inspection.model.ApplicationGroupingModel;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.google.common.base.Splitter;
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
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_REPLICAS;

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
    private static final String FILTER_LIST = """
            oss-chuanyinet-front-static
            ng-fcmb-channel
            ng-wooshpay-channel
            tz-tigozantel-channel
            ng-new-onexbet-channel
            ng-coralpay-channel
            ng-blusalt-channel
            ng-surebet247-channel
            ng-airtel-channel
            ng-accessbet-channel
            ng-baxi-channel
            ng-stanbic-channel
            ng-mtnbucket-channel
            ng-cgate-channel
            ng-dml-channel
            ng-ekedc-channel
            gh-gtb-channel
            ng-ninemobile-channel
            ke-ipay-channel
            ng-mono-channel
            ng-kuda-channel
            ng-betano-channel
            ke-creditinfo-channel
            ng-oasis-channel
            ng-nibss-channel
            ng-axa-channel
            ng-dojah-channel
            ng-betgr8-channel
            ng-nomi-channel
            tz-selcom-biller-channel
            ng-kedc-channel
            ng-credequity-channel
            tz-tigopesa-channel
            tz-halopesa-channel
            tz-halotel-channel
            ng-betwinner-channel
            gh-gtb-transfer-channel
            tz-airtel-ussd-channel
            tz-selcom-channel
            ng-nibsskyc-channel
            ng-sporty-channel
            ng-postpay-channel
            ng-africa365-channel
            ng-fidelity-channel
            ng-nairabet-channel
            ng-betbaba-channel
            tz-vodacom-channel
            ng-betbonanza-channel
            ng-new-qrios-channel
            ng-globucketdata-channel
            ng-wgb-channel
            finance-channels
            ng-irecharge-channel
            ng-buypower-channel
            ng-oneloop-channel
            flutterwave
            ng-tripsdotcom-channel
            ng-wajegame-channel
            ke-cellulantt-channel
            ng-new-buypower-channel
            ng-vertofx-channel
            ng-onexbet-channel
            ng-fdc-channel
            gh-ecg-channel
            ng-betnaija-channel
            ng-geniex-channel
            tz-creditinfo-channel
            ng-jedc-channel
            tz-airtel-channel
            ng-hydrogen-channel
            gh-uba-itex-channel
            ng-monokyc-channel
            ng-smile-channel
            tz-infobip-channel
            file-master
            ng-common-callback
            tecno-sms
            tecno-mail
            tecno-front
            open-api
            send-money
            basic-uid-service
            account-history
            data-portrait-center
            data-center-c
            data-monitor
            scene-directlink-product
            data-view
            user-portrait
            data-carrier-b
            data-carrier-c
            data-buriedpoint-collect
            metersphere
            palmpay-docs
            """;

    public ApplicationGroupingComplianceInspection(NotificationTemplateService notificationTemplateService,
                                                   DingtalkService dingtalkService,
                                                   EdsInstanceHelper edsInstanceHelper,
                                                   EdsConfigService edsConfigService, ListAppGroup listAppGroup,
                                                   EdsAssetService edsAssetService,
                                                   EdsAssetIndexService edsAssetIndexService) {
        super(notificationTemplateService, dingtalkService, edsInstanceHelper, edsConfigService);
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
                    GroupingUtils.grouping(total, groups);
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

    public List<String> toFilterList() {
        return Lists.newArrayList(Splitter.on("\n")
                .split(FILTER_LIST)).stream().filter(StringUtils::hasText).toList();
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
            if (toFilterList().stream()
                    .anyMatch(s -> appNameIndex.getValue()
                            .equals(s))) {
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
                            .equals(KUBERNETES_REPLICAS))
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

