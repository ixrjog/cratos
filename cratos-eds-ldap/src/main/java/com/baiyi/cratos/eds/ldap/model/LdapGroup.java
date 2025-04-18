package com.baiyi.cratos.eds.ldap.model;

import lombok.*;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:16
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class LdapGroup {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Entry(objectClasses = {"groupOfUniqueNames"})
    public static class Group {
        @Attribute
        private String groupId;
        @Attribute(name = "cn")
        private String groupName;
        private String groupDn;
    }

}
