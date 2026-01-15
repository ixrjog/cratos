package com.baiyi.cratos.eds;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.eds.crt.model.CrtSh;
import com.baiyi.cratos.eds.crt.repo.CrtShRepo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/1/14 18:15
 * &#064;Version 1.0
 */
public class CrtShRepoTest extends BaseUnit {

    @Resource
    private CrtShRepo crtShRepo;

    @Test
    void test1() {
        List<CrtSh.CertificateLog> logs = crtShRepo.queryCertificateLogs("palmpay.com");
        System.out.println(logs);
    }

}
