package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessAssetBound;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.view.domain.DomainVO;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:23
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainWrapper extends BaseDataTableConverter<DomainVO.Domain, Domain> implements BaseWrapper<DomainVO.Domain> {

    private final EdsInstanceService instanceService;
    private final BusinessAssetBoundService businessAssetBoundService;
    private final EdsAssetService edsAssetService;

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.ACCOUNT_ENTITY})
    public void wrap(DomainVO.Domain vo) {
        if (IdentityUtils.hasIdentity(vo.getInstanceId())) {
            EdsInstance instance = instanceService.getById(vo.getInstanceId());
            vo.setInstanceName(Optional.ofNullable(instance)
                                       .map(EdsInstance::getInstanceName)
                                       .orElse(""));
        }
        SimpleBusiness business = SimpleBusiness.builder()
                .businessId(vo.getBusinessId())
                .businessType(BusinessTypeEnum.DOMAIN.name())
                .build();
        List<BusinessAssetBound> bounds = businessAssetBoundService.queryByBusiness(business, vo.getDomainType());
        if (!CollectionUtils.isEmpty(bounds)) {
            try {
                EdsAsset asset = edsAssetService.getById(bounds.getFirst()
                                                                 .getAssetId());
                EdsInstance instance = instanceService.getById(asset.getInstanceId());
                vo.setInstanceName(instance.getInstanceName());
            } catch (Exception e) {
            }
        }
    }

}