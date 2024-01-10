package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.param.credential.CredentialParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.facade.CredentialFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.wrapper.CredentialWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:36
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class CredentialFacadeImpl implements CredentialFacade {

    private final CredentialService credentialService;

    private final CredentialWrapper credentialWrapper;

    @Override
    public DataTable<CredentialVO.Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery) {
        DataTable<Credential> table = credentialService.queryCredentialPage(pageQuery);
        return credentialWrapper.wrapToTarget(table);
    }

}
