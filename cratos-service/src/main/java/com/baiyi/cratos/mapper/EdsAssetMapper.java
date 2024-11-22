package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface EdsAssetMapper extends Mapper<EdsAsset> {

    List<EdsAsset> queryPageByParam(EdsInstanceParam.AssetPageQuery pageQuery);

}