package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessDocument;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.service.BusinessDocumentService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseBusinessWrapper;
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
public class BusinessDocWrapper extends BaseDataTableConverter<BusinessDocVO.BusinessDoc, BusinessDocument> implements BaseBusinessWrapper<BusinessDocVO.HasBusinessDocs, BusinessDocVO.BusinessDoc> {

    private final BusinessDocumentService businessDocService;

    @Override
    public void wrap(BusinessDocVO.BusinessDoc vo) {
        // This is a good idea
    }

    @Override
    public void decorateBusiness(BusinessDocVO.HasBusinessDocs biz) {
        biz.setBusinessDocs(businessDocService.selectByBusiness(biz)
                .stream()
                .map(bizDoc -> {
                    BusinessDocVO.BusinessDoc businessDoc = this.convert(bizDoc);
                    // AOP增强
                    delegateWrap(businessDoc);
                    return businessDoc;
                })
                .collect(Collectors.toList()));
    }

}