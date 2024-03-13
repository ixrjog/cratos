package com.baiyi.cratos.eds.business.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.impl.base.BaseAssetToBusinessWrapper;
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
    public EdsAssetVO.AssetToBusiness<User> getToBusinessTarget(EdsAssetVO.Asset asset) {
        LdapPerson.Person person = getAssetModel(asset);
        User user = User.builder()
                .username(person.getUsername())
                .displayName(person.getDisplayName())
                .email(person.getEmail())
                .mobilePhone(person.getMobile())
                .build();
        return EdsAssetVO.AssetToBusiness.<User>builder()
                .target(user)
                .build();
    }

}
