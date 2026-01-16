package com.baiyi.cratos.eds.core.facade.impl;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/3/28 13:44
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsAssetIndexFacadeImpl implements EdsAssetIndexFacade {

    private final EdsAssetIndexService edsAssetIndexService;

    public void saveAssetIndexList(int assetId, List<EdsAssetIndex> edsAssetIndexList) {
        if (CollectionUtils.isEmpty(edsAssetIndexList)) {
            deleteAssetIndexByAssetId(assetId);
            return;
        }
        Map<String, EdsAssetIndex> assetIndexMap = getEdsAssetIndexMap(assetId);
        edsAssetIndexList.stream()
                .filter(Objects::nonNull)
                .forEach(e -> saveAssetIndex(assetIndexMap, e));
        // 删除未匹配的旧索引
        assetIndexMap.values()
                .stream()
                .filter(Objects::nonNull)
                .mapToInt(EdsAssetIndex::getId)
                .forEach(edsAssetIndexService::deleteById);
    }

    private void saveAssetIndex(Map<String, EdsAssetIndex> assetIndexMap, EdsAssetIndex assetIndex) {
        if (assetIndexMap.containsKey(assetIndex.getName())) {
            EdsAssetIndex edsAssetIndex = assetIndexMap.get(assetIndex.getName());
            if (edsAssetIndex != null && !edsAssetIndex.getValue()
                    .equals(assetIndex.getValue())) {
                edsAssetIndex.setValue(assetIndex.getValue());
                edsAssetIndexService.updateByPrimaryKey(edsAssetIndex);
            }
            // 标记为已处理
            assetIndexMap.put(assetIndex.getName(), null);
        } else {
            try {
                edsAssetIndexService.add(assetIndex);
                log.debug("Save asset index: assetId={}, name={}, value={}", assetIndex.getAssetId(), assetIndex.getName(), assetIndex.getValue());
            } catch (Exception exception) {
                log.warn("Repeatedly saving asset index err: assetId={}, name={}, value={}", assetIndex.getAssetId(),
                        assetIndex.getName(), assetIndex.getValue());
            }
        }
    }

    private Map<String, EdsAssetIndex> getEdsAssetIndexMap(int assetId) {
        return edsAssetIndexService.queryIndexByAssetId(assetId)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(EdsAssetIndex::getName, a -> a, (k1, k2) -> k1));
    }

    private void deleteAssetIndexByAssetId(int assetId) {
        edsAssetIndexService.queryIndexByAssetId(assetId)
                .forEach(e -> edsAssetIndexService.deleteById(e.getId()));
    }

    @Override
    public List<EdsAssetIndex> queryAssetIndexById(int assetId) {
        return edsAssetIndexService.queryIndexByAssetId(assetId);
    }

    @Override
    public List<EdsAssetIndex> queryAssetIndexByValue(String value) {
        return edsAssetIndexService.queryIndexByValue(value);
    }

    @Override
    public void deleteIndicesOfAsset(int assetId) {
        edsAssetIndexService.queryIndexByAssetId(assetId)
                .stream()
                .mapToInt(EdsAssetIndex::getId)
                .forEach(edsAssetIndexService::deleteById);
    }

}
