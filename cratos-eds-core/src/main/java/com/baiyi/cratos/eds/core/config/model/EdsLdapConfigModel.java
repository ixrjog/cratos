package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.google.common.base.Joiner;
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

    private static final String RDN = "{}={}";

    @Data
    @NoArgsConstructor
    @Schema
    public static class Ldap implements IEdsConfigModel {
        private String url;
        private String base;
        private LdapManage manager; // 管理员账户
        private LdapUser user;
        private LdapGroup group;
        private EdsInstance edsInstance;

        public String buildUserDn(String username) {
            return Joiner.on(",")
                    .skipNulls()
                    .join(StringFormatter.arrayFormat(RDN, user.getId(), username), user.getDn());
        }

        public String buildGroupDn(String groupName) {
            return Joiner.on(",")
                    .skipNulls()
                    .join(StringFormatter.arrayFormat(RDN, group.getId(), groupName), group.getDn());
        }

    }

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
