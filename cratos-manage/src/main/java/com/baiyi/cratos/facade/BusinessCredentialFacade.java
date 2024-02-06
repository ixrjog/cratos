package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.BaseBusiness;

/**
 * @Author baiyi
 * @Date 2024/2/6 14:10
 * @Version 1.0
 */
public interface BusinessCredentialFacade {

    void revokeBusinessCredential(int id);

    void revokeCredential(int credentialId);

    void revokeBusinessCredential(int credentialId, BaseBusiness.IBusiness business);

    void issueBusinessCredential(int credentialId, BaseBusiness.IBusiness business);

}
