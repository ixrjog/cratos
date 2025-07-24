package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface EdsAssetIndexMapper extends Mapper<EdsAssetIndex> {

    List<EdsAssetIndex> queryIndexByParam(@Param("instanceId") Integer instanceId, @Param("value") String value,
                                          @Param("assetType") String assetType);

    List<EdsAssetIndex> queryIndexByNamePrefixAndAssetType(@Param("namePrefix") String namePrefix,
                                                           @Param("assetType") String assetType,
                                                           @Param("limit") Integer limit);

    List<EdsAssetIndex> queryInvalidIndex();

}