package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.annotation.Sensitive;
import com.baiyi.cratos.common.cred.CredProviderFactory;
import com.baiyi.cratos.common.cred.ICredProvider;
import com.baiyi.cratos.common.util.IdentityUtil;
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
public class CredentialWrapper extends BaseDataTableConverter<CredentialVO.Credential, Credential> implements IBusinessWrapper<CredentialVO.HasCredential, CredentialVO.Credential> {

    private final CredentialService credentialService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG})
    @Sensitive
    public void wrap(CredentialVO.Credential credential) {
        // tips
        ICredProvider iCredProvider = CredProviderFactory.getCredProvider(credential.getCredentialType());
        credential.setTips(iCredProvider != null ? iCredProvider.getDesc() : "n/a");
    }

    @Override
    public void businessWrap(CredentialVO.HasCredential cred) {
        IdentityUtil.validIdentityRun(cred.getCredentialId())
                .withTrue(()->{
                    Credential credential = credentialService.getById(cred.getCredentialId());
                    if (credential == null) {
                        return;
                    }
                    CredentialVO.Credential credentialVO = this.convert(credential);
                    // 重入切面
                    wrapFromProxy(credentialVO);
                    cred.setCredential(credentialVO);
                });
    }

}