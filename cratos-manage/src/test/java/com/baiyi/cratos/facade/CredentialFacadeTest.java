package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.credential.CredentialParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/9 19:21
 * @Version 1.0
 */
public class CredentialFacadeTest extends BaseUnit {

    @Resource
    private CredentialFacade credentialFacade;

    @Test
    void pageTest() {
        CredentialParam.CredentialPageQuery pageQuery = CredentialParam.CredentialPageQuery.builder()
                .page(1)
                .length(10)
                .build();
        DataTable<CredentialVO.Credential> dataTable = credentialFacade.queryCredentialPage(pageQuery);
        System.out.println(pageQuery);
        System.out.println(dataTable);
    }

}