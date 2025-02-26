package com.baiyi.cratos.converter;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 16:54
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EdsIdentityConverter {

    public static LdapPerson.Person toLdapPerson(User user) {
        return LdapPerson.Person.builder()
                .username(user.getUsername())
                .displayName(StringUtils.hasText(user.getDisplayName()) ? user.getDisplayName() : user.getName())
                .email(user.getEmail())
                .mobile(user.getMobilePhone())
                .build();
    }

}
