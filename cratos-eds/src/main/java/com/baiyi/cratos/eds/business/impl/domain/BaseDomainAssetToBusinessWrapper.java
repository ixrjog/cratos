package com.baiyi.cratos.eds.business.impl.domain;

import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.impl.base.BaseAssetToBusinessWrapper;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:56
 * @Version 1.0
 */
public abstract class BaseDomainAssetToBusinessWrapper<B> extends BaseAssetToBusinessWrapper<Domain, B> {

    @Override
    protected Domain getTarget(EdsAssetVO.Asset asset) {
        // AliyunDomain model = getAssetModel(asset);
        return Domain.builder()
                .expiry(asset.getExpiredTime())
                .registrationTime(asset.getCreatedTime())
                .name(asset.getName())
                .valid(true)
                .build();
    }

}
