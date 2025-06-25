package com.baiyi.cratos.util;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.StringFormatter;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/4/19 下午3:12
 * @Version 1.0
 */
public class StringTest extends BaseUnit {

    @Test
    void test1() {
       String t = StringFormatter.eraseLastStr("XXXDFSDGSD-prodsdgsd-prod","-prod");
       System.out.println(t);
    }

    @Test
    void test2() {
        String webSite = StringFormatter.arrayFormat("https://{}/{}", "www.google.com","");
        System.out.println(webSite);
    }

}
