package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
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
public class CredentialWrapper extends BaseDataTableConverter<CredentialVO.Credential, Credential> implements IBaseWrapper<CredentialVO.Credential> {

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG})
    public void wrap(CredentialVO.Credential credential) {
        // This is a good idea
    }

}