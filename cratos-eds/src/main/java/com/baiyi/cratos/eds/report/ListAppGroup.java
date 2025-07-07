package com.baiyi.cratos.eds.report;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.report.model.AppGroupSpec;
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
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_REPLICAS;


/**
 * @Author baiyi
 * @Date 2024/4/18 上午11:42
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ListAppGroup {

    private final EdsAssetIndexService indexService;
    private final EdsAssetService assetService;

    public void doReport() {
        Map<String, AppGroupSpec.GroupSpec> groupingMap = getGroupMap(null, false);
        groupingMap.keySet()
                .forEach(ka -> groupingMap.get(ka)
                        .print());
    }

    public Map<String, AppGroupSpec.GroupSpec> getGroupMap(String queryName, boolean equals) {
        List<EdsAssetIndex> appNameIndices = indexService.queryIndexByName(APP_NAME);
        Map<String, AppGroupSpec.GroupSpec> groupingMap = Maps.newHashMap();
        Map<Integer, EdsAsset> assetMap = Maps.newHashMap();

        for (EdsAssetIndex appNameIndex : appNameIndices) {
            // 按名称过滤
            if (StringUtils.hasText(queryName)) {
                if (!equals && !appNameIndex.getValue()
                        .contains(queryName)) {
                    continue;
                }
                if (equals && !appNameIndex.getValue()
                        .equals(queryName)) {
                    continue;
                }
            }

            // 获取资产信息
            EdsAsset edsAsset = assetMap.containsKey(appNameIndex.getAssetId()) ? assetMap.get(
                    appNameIndex.getAssetId()) : assetService.getById(appNameIndex.getAssetId());
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
