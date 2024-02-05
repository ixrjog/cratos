package com.baiyi.cratos.secutity;

import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.facade.CredentialFacade;
import com.baiyi.cratos.service.UserService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/15 10:26
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class CratosUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private final JasyptPasswordEncoder cratosPasswordEncoder;

    private final CredentialFacade credentialFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(StringFormatter.format("username {} is not found.", username));
        }
        // PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456");
        Credential credential = credentialFacade.getUserPasswordCredential(user);
        return org.springframework.security.core.userdetails.User.withUsername(username)
                .username(username)
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder()
                        .encode(credential.getCredential()))
                .accountLocked(!user.getValid())
                .accountExpired(ExpiredUtil.isExpired(user.getExpiredTime()))
                .credentialsExpired(false)
                .authorities(getUserAuthorities(username))
                .build();
    }

    private List<GrantedAuthority> getUserAuthorities(String username) {
        List<GrantedAuthority> authorities = Lists.newArrayList();
        authorities.add(new SimpleGrantedAuthority("OPS"));
        authorities.add(new SimpleGrantedAuthority("DEV"));
        return authorities;
    }

}
