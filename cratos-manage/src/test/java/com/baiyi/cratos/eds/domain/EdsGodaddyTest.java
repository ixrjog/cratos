package com.baiyi.cratos.eds.domain;

import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.domain.godaddy.model.GodaddyDomain;
import com.baiyi.cratos.eds.domain.godaddy.repo.GodaddyDomainRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/19 15:58
 * &#064;Version 1.0
 */
public class EdsGodaddyTest extends BaseEdsTest<EdsConfigs.Godaddy> {

    @Test
    void test1() {
        List<GodaddyDomain.Domain> domains = GodaddyDomainRepo.queryDomains(getConfig(21));
        System.out.println(domains);
    }

}
