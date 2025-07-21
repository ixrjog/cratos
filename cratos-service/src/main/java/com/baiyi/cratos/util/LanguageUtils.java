package com.baiyi.cratos.util;

import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.util.PropertiesReaderUtils;
import com.baiyi.cratos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/25 09:44
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LanguageUtils {

    @Value("${cratos.language:en-us}")
    private String language;

    private final UserService userService;

    public static final String[] LANGUAGES = {"zh-cn", "en-us"};

    public String getLanguageOf(User user) {
        if (user == null || user.getLang() == null) {
            return getSysLanguage();
        }
        return Arrays.asList(LANGUAGES)
                .contains(user.getLang()) ? user.getLang() : getSysLanguage();
    }

    public String getSysLanguage() {
        return language;
    }

    public String getMessage(User user, String key) {
        String userLanguage = getLanguageOf(user);
        if (userLanguage.equals("zh-cn")) {
            return PropertiesReaderUtils.getZhCnMessage(key);
        } else {
            return PropertiesReaderUtils.getEnUsMessage(key);
        }
    }

    public String getFormattedMessage(User user, String key, Object... args) {
        String userLanguage = getLanguageOf(user);
        if (userLanguage.equals("zh-cn")) {
            return PropertiesReaderUtils.getFormattedZhCnMessage(key, args);
        } else {
            return PropertiesReaderUtils.getFormattedEnUsMessage(key, args);
        }
    }

    public String getMessage(String key) {
        String userLanguage = getLanguageOf(userService.getByUsername(SessionUtils.getUsername()));
        if (userLanguage.equals("zh-cn")) {
            return PropertiesReaderUtils.getZhCnMessage(key);
        } else {
            return PropertiesReaderUtils.getEnUsMessage(key);
        }
    }

    public String getFormattedMessage(String key, Object... args) {
        String userLanguage = getLanguageOf(userService.getByUsername(SessionUtils.getUsername()));
        if (userLanguage.equals("zh-cn")) {
            return PropertiesReaderUtils.getFormattedZhCnMessage(key, args);
        } else {
            return PropertiesReaderUtils.getFormattedEnUsMessage(key, args);
        }
    }

}
