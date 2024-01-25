package com.baiyi.cratos.service;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import jakarta.annotation.Resource;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/24 18:41
 * @Version 1.0
 */
public class BusinessTagServiceTest extends BaseUnit {

    @Resource
    private BusinessTagService businessTagService;

    @Resource
    private CertificateService certificateService;

    @Test
    void test() {
        List<Integer> ids = businessTagService.queryBusinessIdsByParam(BusinessTypeEnum.CERTIFICATE, Lists.newArrayList(1));
        System.out.println(ids);
        List<Certificate> certificates = certificateService.queryByIds(ids);
        System.out.println(certificates);
    }

}
