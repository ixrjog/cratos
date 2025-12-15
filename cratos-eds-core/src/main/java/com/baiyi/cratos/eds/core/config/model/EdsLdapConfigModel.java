package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:13
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsLdapConfigModel {

    public static final String RDN = "{}={}";

    @Data
    @NoArgsConstructor
    @Schema
    public static class LdapManage {
        private String dn;  // 管理员账户
        private String password; // 管理员账户密码
    }

    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class LdapUser extends BaseLdapObject {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class LdapGroup extends BaseLdapObject {
        private String memberAttribute;
        private String memberFormat;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class BaseLdapObject {
        private String id;
        private String dn;
        private String objectClass;
    }

}
