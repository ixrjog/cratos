package com.baiyi.cratos.eds.report;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.report.model.AppGroupingSpecifications;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.domain.constant.Global.APP_NAME;

/**
 * @Author baiyi
 * @Date 2024/4/18 上午11:42
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeploymentSubGroupReport {

    private final EdsAssetIndexService indexService;

    private final EdsAssetService assetService;

    public void doReport() {
        Map<String, AppGroupingSpecifications.GroupingSpecifications> groupingMap = getGroupingMap(null);
        groupingMap.keySet()
                .forEach(ka -> groupingMap.get(ka)
                        .print());
    }

    public Map<String, AppGroupingSpecifications.GroupingSpecifications> getGroupingMap(String queryName) {
        List<EdsAssetIndex> appNameIndices = indexService.queryIndexByName(APP_NAME);
        Map<String, AppGroupingSpecifications.GroupingSpecifications> groupingMap = Maps.newHashMap();
        Map<Integer, EdsAsset> assetMap = Maps.newHashMap();

        for (EdsAssetIndex appNameIndex : appNameIndices) {
            // 按名称过滤
            if (StringUtils.hasText(queryName) && !appNameIndex.getValue()
                    .contains(queryName)) {
                continue;
            }

            // 获取资产信息
            EdsAsset edsAsset = assetMap.containsKey(appNameIndex.getAssetId()) ? assetMap.get(appNameIndex.getAssetId()) : assetService.getById(appNameIndex.getAssetId());
            if (edsAsset == null) {
                continue;
            }
            assetMap.put(appNameIndex.getAssetId(), edsAsset);

            // 获取资产的所有索引
            List<EdsAssetIndex> assetIndices = indexService.queryIndexByAssetId(appNameIndex.getAssetId());

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
                            .equals("replicas"))
                    .findFirst();
            if (replicasIndexOptional.isEmpty()) {
                continue;
            }

            AppGroupingSpecifications.GroupingSpecifications groupingSpecifications;
            if (groupingMap.containsKey(appNameIndex.getValue())) {
                groupingSpecifications = groupingMap.get(appNameIndex.getValue());
            } else {
                groupingSpecifications = AppGroupingSpecifications.GroupingSpecifications.builder()
                        .appName(appNameIndex.getValue())
                        .env(envIndexOptional.get()
                                .getValue())
                        .build();
            }
            // 通过资产名称来设置分组副本数

            AppGroupingSpecifications.Grouping grouping = AppGroupingSpecifications.Grouping.builder()
                    .name(edsAsset.getName())
                    .replicas(Integer.parseInt(replicasIndexOptional.get()
                            .getValue()))
                    .build();

            // 去掉环境表情
            String newGroupingName = StringFormatter.eraseLastStr(edsAsset.getName(), "-" + envIndexOptional.get()
                    .getValue());
            // 分组名称 = 应用名称
            if (appNameIndex.getValue()
                    .equals(newGroupingName)) {
                groupingSpecifications.setG1(grouping);
                groupingMap.put(appNameIndex.getValue(), groupingSpecifications);
                continue;
            }

            if (newGroupingName.endsWith("-canary")) {
                grouping.setReplicas(1);
                groupingSpecifications.setCanary(grouping);
                groupingMap.put(appNameIndex.getValue(), groupingSpecifications);
                continue;
            }

            if (newGroupingName.endsWith("-1")) {
                groupingSpecifications.setG1(grouping);
                groupingMap.put(appNameIndex.getValue(), groupingSpecifications);
                continue;
            }

            if (newGroupingName.endsWith("-2")) {
                groupingSpecifications.setG2(grouping);
                groupingMap.put(appNameIndex.getValue(), groupingSpecifications);
                continue;
            }

            if (newGroupingName.endsWith("-3")) {
                groupingSpecifications.setG3(grouping);
                groupingMap.put(appNameIndex.getValue(), groupingSpecifications);
            }

            if (newGroupingName.endsWith("-4")) {
                groupingSpecifications.setG4(grouping);
                groupingMap.put(appNameIndex.getValue(), groupingSpecifications);
            }
        }
        return groupingMap;
    }

}
