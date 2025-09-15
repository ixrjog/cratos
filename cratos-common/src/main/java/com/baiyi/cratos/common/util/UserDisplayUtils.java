package com.baiyi.cratos.common.util;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/8 11:14
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class UserDisplayUtils {

    public static String getDisplayName(User user) {
        if (ValidationUtils.isEmail(user.getEmail())) {
            return StringFormatter.arrayFormat("{}<{}|{}>", user.getUsername(), user.getDisplayName(), user.getEmail());
        } else {
            return StringFormatter.arrayFormat("{}<{}>", user.getUsername(), user.getDisplayName());
        }
    }

}
