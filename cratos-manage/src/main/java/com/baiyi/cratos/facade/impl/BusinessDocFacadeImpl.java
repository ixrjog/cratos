package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.BusinessDocument;
import com.baiyi.cratos.domain.param.business.BusinessParam;
import com.baiyi.cratos.domain.param.doc.BusinessDocParam;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.facade.BusinessDocFacade;
import com.baiyi.cratos.service.BusinessDocService;
import com.baiyi.cratos.wrapper.BusinessDocWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/1/9 09:54
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessDocFacadeImpl implements BusinessDocFacade {

    private final BusinessDocService businessDocService;

    private final BusinessDocWrapper businessDocWrapper;

    @Override
    public List<BusinessDocVO.BusinessDoc> getBusinessDocByBusiness(BusinessParam.GetByBusiness getByBusiness) {
        List<BusinessDocument> tags = businessDocService.selectByBusiness(getByBusiness);
        return tags.stream()
                .map(t -> {
                    BusinessDocVO.BusinessDoc doc = businessDocWrapper.convert(t);
                    businessDocWrapper.wrap(doc);
                    return doc;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addBusinessDoc(BusinessDocParam.AddBusinessDoc addBusinessDoc) {
        businessDocService.add(addBusinessDoc.toTarget());
    }

    @Override
    public void updateBusinessDoc(BusinessDocParam.UpdateBusinessDoc updateBusinessDoc) {
        businessDocService.updateByPrimaryKey(updateBusinessDoc.toTarget());
    }

    @Override
    public void deleteById(int id) {
        businessDocService.deleteById(id);
    }

}
