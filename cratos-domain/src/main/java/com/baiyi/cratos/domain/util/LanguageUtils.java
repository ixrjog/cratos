package com.baiyi.cratos.domain.util;

import com.baiyi.cratos.domain.generator.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/25 09:44
 * &#064;Version 1.0
 */
@Component
public class LanguageUtils {

    @Value("${cratos.language:en-us}")
    private String language;

    public String getUserLanguage(User user) {
        if (StringUtils.hasText(user.getLang())) {
            return user.getLang();
        }
        return language;
    }

    public String getSysLanguage() {
        return language;
    }

}
