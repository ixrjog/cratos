package com.baiyi.cratos.service;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.BusinessDocument;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/3/18 17:36
 * @Version 1.0
 */
public class BusinessDocumentServiceTest extends BaseUnit {

    @Resource
    private BusinessDocumentService businessDocumentService;

    @Test
    void test() {
        BusinessDocument businessDocument = businessDocumentService.getById(22);
        businessDocumentService.delete(businessDocument);
    }

}

