package com.baiyi.cratos.eds.domain;

import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.domain.gandi.model.GandiDomain;
import com.baiyi.cratos.eds.domain.gandi.repo.GandiDomainRepo;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/19 15:55
 * &#064;Version 1.0
 */
public class EdsGandiTest extends BaseEdsTest<EdsConfigs.Gandi> {

    @Test
    void test1() {
        List<GandiDomain.Domain> domains = GandiDomainRepo.queryDomains(getConfig(20));
        System.out.println(domains);
    }

}
