package com.baiyi.cratos.eds.core.facade.impl;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.exception.DaoServiceException;
import com.baiyi.cratos.service.EdsAssetIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
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
        edsAssetIndexList.forEach(e -> {
            if (e == null) {
                return;
            }
            saveAssetIndex(assetIndexMap, e);
        });
        assetIndexMap.keySet()
                .forEach(key -> edsAssetIndexService.deleteById(assetIndexMap.get(key)
                        .getId()));
    }

    private void saveAssetIndex(Map<String, EdsAssetIndex> assetIndexMap, EdsAssetIndex e) {
        if (assetIndexMap.containsKey(e.getName())) {
            EdsAssetIndex edsAssetIndex = assetIndexMap.get(e.getName());
            if (!edsAssetIndex.getValue()
                    .equals(e.getValue())) {
                edsAssetIndex.setValue(e.getValue());
                edsAssetIndexService.updateByPrimaryKey(edsAssetIndex);
            }
            assetIndexMap.remove(e.getName());
        } else {
            try {
                edsAssetIndexService.add(e);
                log.debug("Save asset index: assetId={}, name={}, value={}", e.getAssetId(), e.getName(), e.getValue());
            } catch (DuplicateKeyException | DaoServiceException sqlException) {
                log.debug("Repeatedly saving asset index err: assetId={}, name={}, value={}", e.getAssetId(),
                        e.getName(), e.getValue());
            }
        }
    }

    private Map<String, EdsAssetIndex> getEdsAssetIndexMap(int assetId) {
        return edsAssetIndexService.queryIndexByAssetId(assetId)
                .stream()
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

}
