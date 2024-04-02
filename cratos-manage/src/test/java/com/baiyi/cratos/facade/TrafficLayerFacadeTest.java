package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
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
        TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails = TrafficLayerRecordParam.QueryRecordDetails.builder()
                .domainId(1)
                .envName("dev")
                .build();
        TrafficLayerRecordVO.RecordDetails recordDetails = trafficLayerFacade.queryRecordDetails(queryRecordDetails);
        System.out.println(recordDetails);
    }


    @Test
    void test2() {
        TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails = TrafficLayerRecordParam.QueryRecordDetails.builder()
                .domainId(1)
                .envName("daily")
                .build();
        String table = trafficLayerFacade.queryRecordDetailsStringTable(queryRecordDetails);
        System.out.println(table);
    }


}
