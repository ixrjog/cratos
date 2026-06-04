package com.baiyi.cratos.wrapper.acme;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.domain.view.acme.AcmeOrderVO;
import com.baiyi.cratos.service.acme.AcmeOrderService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/30 10:02
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.ACME_ORDER)
public class AcmeOrderWrapper extends BaseDataTableConverter<AcmeOrderVO.Order, AcmeOrder> implements BaseBusinessDecorator<AcmeOrderVO.HasRecentOrder, AcmeOrderVO.Order> {

    private final AcmeOrderService orderService;

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.ACME_ACCOUNT, BusinessTypeEnum.ACME_DOMAIN})
    public void wrap(AcmeOrderVO.Order vo) {
    }

    @Override
    public void decorateBusiness(AcmeOrderVO.HasRecentOrder hasBusiness) {
        if (IdentityUtils.hasIdentity(hasBusiness.getAcmeDomainId())) {
            AcmeOrder acmeOrder = orderService.getRecentOrder(hasBusiness.getAcmeDomainId());
            // 不能循环依赖
            AcmeOrderVO.Order orderVO = this.convert(acmeOrder);
            hasBusiness.setRecentOrder(orderVO);
        }
    }

}