package com.baiyi.cratos.facade.cred;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.cred.base.BaseCredProvider;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 上午10:05
 * &#064;Version 1.0
 */
@Component
public class GoogleAdcCredProvider extends BaseCredProvider {

    @Override
    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.GOOGLE_ADC;
    }

    @Override
    protected DictBuilder newDictBuilder(Credential credential) {
        return DictBuilder.newBuilder();
    }

}
