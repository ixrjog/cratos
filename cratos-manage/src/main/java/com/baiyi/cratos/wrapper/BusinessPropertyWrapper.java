package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessProperty;
import com.baiyi.cratos.domain.view.business.BusinessPropertyVO;
import com.baiyi.cratos.service.BusinessPropertyService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/3/22 10:16
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.BUSINESS_PROPERTY)
public class BusinessPropertyWrapper extends BaseDataTableConverter<BusinessPropertyVO.BusinessProperty, BusinessProperty> implements IBusinessWrapper<BusinessPropertyVO.HasBusinessProperties, BusinessPropertyVO.BusinessProperty> {

    private final BusinessPropertyService businessPropertyService;

    @Override
    @BusinessWrapper(types = BusinessTypeEnum.BUSINESS_PROPERTY)
    public void wrap(BusinessPropertyVO.BusinessProperty vo) {
        // This is a good idea
    }

    @Override
    public void businessWrap(BusinessPropertyVO.HasBusinessProperties businessProperties) {
        businessProperties.setBusinessProperties(businessPropertyService.selectByBusiness(businessProperties)
                .stream()
                .map(bizTag -> {
                    BusinessPropertyVO.BusinessProperty businessProperty = this.convert(bizTag);
                    // AOP增强
                    wrapFromProxy(businessProperty);
                    return businessProperty;
                })
                .collect(Collectors.toList()));
    }

}