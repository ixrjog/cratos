package com.baiyi.cratos.util;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.util.dnsgoogle.DnsGoogleUtils;
import com.baiyi.cratos.domain.util.dnsgoogle.model.DnsGoogleModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/16 14:27
 * &#064;Version 1.0
 */
public class DnsGoogleUtilsTest extends BaseUnit {

    @Resource
    private DnsGoogleUtils dnsGoogleUtils;

    @Test
    void test() {
        DnsGoogleModel.DnsResolve dnsResolve = dnsGoogleUtils.resolve("h5.palmpay.app");
        System.out.println(dnsResolve);
    }

}
