package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.annotation.Sensitive;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:58
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CREDENTIAL)
public class CredentialWrapper extends BaseDataTableConverter<CredentialVO.Credential, Credential> implements IBusinessWrapper<CredentialVO.ICred, CredentialVO.Credential> {

    private final CredentialService credentialService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG})
    @Sensitive
    public void wrap(CredentialVO.Credential credential) {
        // This is a good idea
    }

    @Override
    public void businessWrap(CredentialVO.ICred cred) {
        CredentialVO.Credential credential = this.convert(credentialService.getById(cred.getCredId()));
        wrapFromProxy(credential);
    }

}