package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessDocument;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.service.BusinessDocService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/1/9 10:30
 * @Version 1.0
 */
@Slf4j
@Component
// 懒加载防止循环依赖
//@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.BUSINESS_DOC)
public class BusinessDocWrapper extends BaseDataTableConverter<BusinessDocVO.BusinessDoc, BusinessDocument> implements IBusinessWrapper<BusinessDocVO.IBusinessDocs, BusinessDocVO.BusinessDoc> {

    private final BusinessDocService businessDocService;

    @Override
    public void wrap(BusinessDocVO.BusinessDoc businessDoc) {
        // This is a good idea
    }

    @Override
    public void businessWrap(BusinessDocVO.IBusinessDocs businessDocs) {
        businessDocs.setBusinessDocs(businessDocService.selectByBusiness(businessDocs)
                .stream()
                .map(bizDoc -> {
                    BusinessDocVO.BusinessDoc businessDoc = this.convert(bizDoc);
                    // AOP增强
                    wrapFromProxy(businessDoc);
                    return businessDoc;
                })
                .collect(Collectors.toList()));
    }

}