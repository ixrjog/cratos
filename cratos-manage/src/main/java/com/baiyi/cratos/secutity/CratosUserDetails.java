package com.baiyi.cratos.secutity;

import com.baiyi.cratos.domain.generator.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/15 10:34
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
public class CratosUserDetails extends User implements UserDetails {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private List<GrantedAuthority> authorities;

    @Schema(description = "账户是否过期")
    private boolean accountNonExpired;

    @Schema(description = "账户是否锁定")
    private boolean accountNonLocked;

    @Schema(description = "凭据是否过期")
    private boolean credentialsNonExpired;

    @Schema(description = "账户是否可用")
    private boolean enabled;

}
