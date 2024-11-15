package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.mapper.EdsAssetIndexMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import lombok.NonNull;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/27 14:33
 * @Version 1.0
 */
public interface EdsAssetIndexService extends BaseUniqueKeyService<EdsAssetIndex, EdsAssetIndexMapper> {

    List<EdsAssetIndex> queryIndexByAssetId(int assetId);

    int selectCountByAssetId(int assetId);

    List<EdsAssetIndex> queryInstanceIndexByNameAndValue(@NonNull Integer instanceId, @NonNull String name,
                                                         @NonNull String value);

    List<EdsAssetIndex> queryIndexByName(@NonNull String name);

    List<EdsAssetIndex> queryIndexByValue(@NonNull String value);

    List<EdsAssetIndex> queryIndexByNameAndValue(@NonNull String name, @NonNull String value);

    List<EdsAssetIndex> queryIndexByParam(@NonNull Integer instanceId, @NonNull String value,
                                          @NonNull String assetType);

    List<EdsAssetIndex> queryIndexByParam(@NonNull String namePrefix, @NonNull String assetType, int limit);

    EdsAssetIndex getByAssetIdAndName(int assetId,String name);

}
