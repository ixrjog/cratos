package com.baiyi.cratos.eds.business.wrapper.impl.persion;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.wrapper.impl.BaseAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/12 10:40
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.USER)
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.LDAP, assetType = EdsAssetTypeEnum.LDAP_PERSON)
public class LdapPersonAssetToBusinessWrapper extends BaseAssetToBusinessWrapper<User, LdapPerson.Person> {

    @Override
    protected User getTarget(EdsAssetVO.Asset asset) {
        LdapPerson.Person model = getAssetModel(asset);
        return User.builder()
                .username(model.getUsername())
                .name(model.getDisplayName())
                .displayName(model.getDisplayName())
                .email(model.getEmail())
                .mobilePhone(model.getMobile())
                .valid(true)
                .build();
    }

}
