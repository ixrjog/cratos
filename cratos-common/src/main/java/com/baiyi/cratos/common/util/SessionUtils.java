package com.baiyi.cratos.common.util;

import com.baiyi.cratos.common.session.UserSessionContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/18 10:36
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtils {

    public static void setUsername(String username) {
        if (StringUtils.hasText(username)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null);
            SecurityContextHolder.getContext()
                    .setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    public static String getUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();
            return authentication.getName();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前会话语言
     *
     * @return
     */
    public static String getSessionLanguage() {
        return UserSessionContext.getLanguage();
    }

    public static void setSessionLanguage(String language) {
        UserSessionContext.setLanguage(language);
    }

}
