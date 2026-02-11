package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.AssetMaturity;
import com.baiyi.cratos.domain.param.http.asset.AssetMaturityParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AssetMaturityMapper extends Mapper<AssetMaturity> {

    List<AssetMaturity> queryPageByParam(AssetMaturityParam.AssetMaturityPageQueryParam pageQuery);

}