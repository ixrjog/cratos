package com.baiyi.cratos.common.util;

import com.google.common.base.Joiner;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2023/7/14 13:23
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class StringFormatter {

    public static String arrayFormat(String str, Object... args) {
        return MessageFormatter.arrayFormat(str, args)
                .getMessage();
    }

    public static String format(String str, Object arg) {
        return MessageFormatter.format(str, arg)
                .getMessage();
    }

    // 剧中输出 ============================== Message ==============================

    private static final String BAR = "========================================================================================================================";

    public static String inDramaFormat(String message) {
        if (StringUtils.isEmpty(message)) {
            return BAR;
        }
        int length = message.length();
        int symbolLength = (118 - (length + 1)) / 2;
        String symbol = BAR.substring(0, symbolLength);
        if (isEven(length)) {
            return Joiner.on(" ")
                    .join(symbol, message, symbol);
        } else {
            return Joiner.on(" ")
                    .join(symbol, message, BAR.substring(0, symbolLength + 1));
        }
    }

    private static boolean isEven(int num) {
        return (num & 1) == 0;
    }

    public static String eraseLastStr(String str1, String str2) {
        if (!org.springframework.util.StringUtils.hasText(str1)) {
            return "";
        }
        if (!org.springframework.util.StringUtils.hasText(str2)) {
            return str1;
        }
        if (!str1.endsWith(str2)) {
            return str1;
        }
        return str1.substring(0, str1.length() - str2.length());
    }

}