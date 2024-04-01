package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainRecordVO;
import com.baiyi.cratos.facade.proxy.TrafficLayerProxy;
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

    @Resource
    private TrafficLayerProxy trafficLayerProxy;

    @Test
    void test() {
        TrafficLayerDomainRecordParam.QueryRecordDetails queryRecordDetails = TrafficLayerDomainRecordParam.QueryRecordDetails.builder()
                .domainId(1)
                .envName("dev")
                .build();
        TrafficLayerDomainRecordVO.RecordDetails recordDetails = trafficLayerFacade.queryRecordDetails(queryRecordDetails);
        System.out.println(recordDetails);
    }

}
