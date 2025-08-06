package com.baiyi.cratos.eds.business.wrapper.impl.domain.base;

import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.wrapper.impl.BaseAssetToBusinessWrapper;
import com.baiyi.cratos.service.BusinessAssetBindService;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:56
 * @Version 1.0
 */
public abstract class BaseDomainAssetToBusinessWrapper<B> extends BaseAssetToBusinessWrapper<Domain, B> {

    public BaseDomainAssetToBusinessWrapper(BusinessAssetBindService businessAssetBindService) {
        super(businessAssetBindService);
    }

    abstract protected String getDomainType();

    @Override
    protected Domain toTarget(EdsAssetVO.Asset asset) {
        // AliyunDomain model = getAssetModel(asset);
        return Domain.builder()
                .expiry(asset.getExpiredTime())
                .registrationTime(asset.getCreatedTime())
                .domainType(getDomainType())
                .name(asset.getName())
                .valid(true)
                .build();
    }

}
