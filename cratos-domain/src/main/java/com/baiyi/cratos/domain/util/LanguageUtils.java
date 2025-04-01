package com.baiyi.cratos.domain.util;

import com.baiyi.cratos.domain.generator.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/25 09:44
 * &#064;Version 1.0
 */
@Component
public class LanguageUtils {

    @Value("${cratos.language:en-us}")
    private String language;

    private static final String[] LANGUAGES = {"zh-cn", "en-us"};

    public String getLanguageOf(User user) {
        return Arrays.asList(LANGUAGES)
                .contains(user.getLang()) ? user.getLang() : getSysLanguage();
    }

    public String getSysLanguage() {
        return language;
    }

}
