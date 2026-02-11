package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.query.EdsAssetQuery;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface EdsAssetMapper extends Mapper<EdsAsset> {

    List<EdsAsset> queryPageByParam(EdsInstanceParam.AssetPageQueryParam param);

    List<Integer> queryUserPermissionBusinessIds(EdsAssetQuery.QueryUserPermissionBusinessIdParam param);

    List<EdsAsset> queryUserPermissionPageByParam(EdsAssetQuery.UserPermissionPageQueryParam param);

}