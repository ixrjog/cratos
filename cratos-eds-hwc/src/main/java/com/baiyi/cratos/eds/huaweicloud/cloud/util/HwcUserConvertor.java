package com.baiyi.cratos.eds.huaweicloud.cloud.util;

import com.huaweicloud.sdk.iam.v3.model.KeystoneCreateUserResult;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListUsersResult;
import com.huaweicloud.sdk.iam.v3.model.Links;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/5 11:29
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HwcUserConvertor {

    public static KeystoneListUsersResult to(KeystoneCreateUserResult createUserResult) {
        KeystoneListUsersResult keystoneListUsersResult = new KeystoneListUsersResult();
        keystoneListUsersResult.setEnabled(createUserResult.getEnabled());
        keystoneListUsersResult.setId(createUserResult.getId());
        keystoneListUsersResult.setName(createUserResult.getName());
        keystoneListUsersResult.setDescription(createUserResult.getDescription());
        keystoneListUsersResult.setDomainId(createUserResult.getDomainId());
        Links links = new Links();
        links.setSelf(createUserResult.getLinks()
                .getSelf());
        keystoneListUsersResult.setLinks(links);
        keystoneListUsersResult.setPasswordExpiresAt(createUserResult.getPasswordExpiresAt());
        return keystoneListUsersResult;
    }

}
