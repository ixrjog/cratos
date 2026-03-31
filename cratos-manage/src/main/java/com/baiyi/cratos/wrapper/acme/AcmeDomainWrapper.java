package com.baiyi.cratos.wrapper.acme;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.view.acme.AcmeDomainVO;
import com.baiyi.cratos.service.acme.AcmeDomainService;
import com.baiyi.cratos.service.acme.AcmeOrderService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/27 15:49
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.ACME_DOMAIN)
public class AcmeDomainWrapper extends BaseDataTableConverter<AcmeDomainVO.Domain, AcmeDomain> implements BaseBusinessDecorator<AcmeDomainVO.HasAcmeDomain, AcmeDomainVO.Domain> {

    private final AcmeDomainService acmeDomainService;
    private final AcmeOrderService acmeOrderService;

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.ACME_ACCOUNT, BusinessTypeEnum.EDS_INSTANCE})
    public void wrap(AcmeDomainVO.Domain vo) {
        vo.setResourceCount(makeResourceCountForAcmeOrder(vo));
    }

    private Map<String, Integer> makeResourceCountForAcmeOrder(AcmeDomainVO.Domain vo) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(BusinessTypeEnum.ACME_ORDER.name(), acmeOrderService.countByDomainId(vo.getId()));
        return resourceCount;
    }

    @Override
    public void decorateBusiness(AcmeDomainVO.HasAcmeDomain hasBusiness) {
        if (IdentityUtils.hasIdentity(hasBusiness.getAcmeDomainId())) {
            Optional.ofNullable(acmeDomainService.getById(hasBusiness.getAcmeDomainId()))
                    .ifPresent(acmeDomain -> {
                        AcmeDomainVO.Domain domainVO = convert(acmeDomain);
                        delegateWrap(domainVO);
                        hasBusiness.setAcmeDomain(domainVO);
                    });
        }
    }

}