package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.credential.CredentialParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:35
 * @Version 1.0
 */
public interface CredentialFacade {

    DataTable<CredentialVO.Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery);

    void setCredentialValidById(int id);

    /**
     * 吊销凭据
     *
     * @param id
     */
    void revokeCredentialById(int id);

    List<Credential> queryCredentialByBusiness(BaseBusiness.IBusiness business);

    void createBusinessCredential(Credential credential, BaseBusiness.IBusiness business);

    Credential getUserPasswordCredential(User user);

    void addCredential(CredentialParam.AddCredential addCredential);

    void updateCredential(CredentialParam.UpdateCredential updateCredential);

}
