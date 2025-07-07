package com.baiyi.cratos.util;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.util.PropertiesReaderUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/7 16:05
 * &#064;Version 1.0
 */
public class LanguageUtilsTest extends BaseUnit {

    @Resource
    private LanguageUtils languageUtils;

    @Test
    void test1() {
        String enUsMessage = PropertiesReaderUtils.getEnUsMessage("workorder.create.ticket.limit.exception.message");
        String zhCnMessage = PropertiesReaderUtils.getZhCnMessage("workorder.create.ticket.limit.exception.message");

        System.out.println(enUsMessage);
        System.out.println(zhCnMessage);
    }

    @Test
    void test2() {
        String enUsMessage = PropertiesReaderUtils.getFormattedEnUsMessage("workorder.create.ticket.limit.exception.message","XXXXXXX");
        String zhCnMessage = PropertiesReaderUtils.getFormattedZhCnMessage("workorder.create.ticket.limit.exception.message","XXXXXXX");

        System.out.println(enUsMessage);
        System.out.println(zhCnMessage);
    }

}
