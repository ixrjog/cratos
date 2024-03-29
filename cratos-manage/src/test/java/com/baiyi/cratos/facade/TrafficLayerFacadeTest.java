package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainRecordVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/3/29 16:04
 * @Version 1.0
 */
public class TrafficLayerFacadeTest extends BaseUnit {

    @Resource
    private TrafficLayerFacade trafficLayerFacade;

    @Test
    void test() {
        TrafficLayerDomainRecordVO.RecordDetails recordDetails = trafficLayerFacade.queryRecordDetails(1, "dev");
        System.out.println(recordDetails);
    }

}
