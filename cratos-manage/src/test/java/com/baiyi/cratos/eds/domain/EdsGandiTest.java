package com.baiyi.cratos.eds.domain;

import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.domain.gandi.model.GandiDomain;
import com.baiyi.cratos.eds.domain.gandi.model.GandiLiveDNS;
import com.baiyi.cratos.eds.domain.gandi.repo.GandiDomainRepo;
import com.baiyi.cratos.eds.domain.gandi.repo.GandiLiveDNSRepo;
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

    @Test
    void test2() {
        List<GandiLiveDNS.Record> records = GandiLiveDNSRepo.queryRecords(getConfig(20), "flexiglobal.vip");
        System.out.println(records);
    }

    @Test
    void test3() {
//        List<GandiLiveDNS.Record> records =  GandiServiceFactory.createLiveDNSService(getConfig(20))
//                .queryRecordsByNameAndType("flexiglobal.vip","traffic-test","CNAME");
//        System.out.println(records);
    }

    @Test
    void test4() {
//       String records =  GandiServiceFactory.createLiveDNSService(getConfig(20)).
//                .queryRecordsByNameAndType2("flexiglobal.vip","traffic-test.flexiglobal.vip","CNAME");
//        System.out.println(records);
    }

}
