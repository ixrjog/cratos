package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.credential.CredentialParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:35
 * @Version 1.0
 */
public interface CredentialFacade {

    DataTable<CredentialVO.Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery);

}
