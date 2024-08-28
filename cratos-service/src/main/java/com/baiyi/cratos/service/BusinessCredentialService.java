package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.service.base.BaseBusinessService;
import com.baiyi.cratos.service.base.HasUniqueKey;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/12 13:44
 * @Version 1.0
 */
public interface BusinessCredentialService extends BaseBusinessService<BusinessCredential>, HasUniqueKey<BusinessCredential> {

    List<BusinessCredential> queryByCredentialId(int credentialId);

}
