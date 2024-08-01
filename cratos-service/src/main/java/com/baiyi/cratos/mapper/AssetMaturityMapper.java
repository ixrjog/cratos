package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.AssetMaturity;
import com.baiyi.cratos.domain.param.asset.AssetMaturityParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AssetMaturityMapper extends Mapper<AssetMaturity> {

    List<AssetMaturity> queryPageByParam(AssetMaturityParam.AssetMaturityPageQueryParam pageQuery);

}