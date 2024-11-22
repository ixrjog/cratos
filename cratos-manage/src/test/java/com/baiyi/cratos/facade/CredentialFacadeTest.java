package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.credential.CredentialParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.service.UserService;
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

    @Resource
    private UserService userService;

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

    @Test
    void test2() {
        User user = userService.getByUsername("baiyi-test");
        credentialFacade.getUserPasswordCredential(user);
    }

}