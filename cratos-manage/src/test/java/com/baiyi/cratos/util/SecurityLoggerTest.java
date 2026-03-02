package com.baiyi.cratos.util;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.SecurityLogger;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/28 14:08
 * &#064;Version 1.0
 */
public class SecurityLoggerTest extends BaseUnit {

    @Test
    void logTest() {
        //     public static void log(String eventType, String user, String action, String resource) {


        SecurityLogger.log("LOGIN", "baiyi", "login_success", "User login Cratos");

        SecurityLogger.log("LOGIN", "baiyi", "login_success", "User login Cratos SSH-Server");


    }
}
