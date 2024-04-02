package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.facade.TrafficLayerFacade;
import com.baiyi.cratos.facade.proxy.TrafficLayerProxy;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import com.baiyi.cratos.wrapper.TrafficLayerRecordWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.facade.proxy.TrafficLayerProxy.RULES;

/**
 * @Author baiyi
 * @Date 2024/3/29 13:48
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficLayerFacadeImpl implements TrafficLayerFacade {

    private final TrafficLayerDomainRecordService recordService;

    private final TrafficLayerRecordWrapper recordWrapper;

    private final TrafficLayerProxy trafficLayerProxy;

    public final static String[] RECORD_TABLE_FIELD_NAME = {"Record Name", "Env Name", "Route Traffic To", "Origin Server"};

    public final static String[] LB_TABLE_FIELD_NAME = {"Load Balancer Name", "DNS Name", "Load Balancer Provider"};

    public final static String[] RULE_TABLE_FIELD_NAME = {"Ingress Rule", "Kubernetes Service"};

    @Override
    public TrafficLayerRecordVO.RecordDetails queryRecordDetails(TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails) {
        TrafficLayerDomainRecord uniqueKey = queryRecordDetails.toTrafficLayerRecord();
        TrafficLayerDomainRecord trafficLayerDomainRecord = recordService.getByUniqueKey(uniqueKey);
        if (trafficLayerDomainRecord == null) {
            return TrafficLayerRecordVO.RecordDetails.NOT_FOUND;

        }
        TrafficLayerRecordVO.RecordDetails recordDetails = TrafficLayerRecordVO.RecordDetails.builder()
                .recordId(trafficLayerDomainRecord.getId())
                .record(recordWrapper.wrapToTarget(trafficLayerDomainRecord))
                .originServer(trafficLayerProxy.buildOriginServer(trafficLayerDomainRecord.getRecordName(), trafficLayerDomainRecord.getOriginServer()))
                .build();
        TrafficLayerRecordVO.TableDetails tableDetails = buildTableDetails(recordDetails);
        recordDetails.setTableDetails(tableDetails);
        return recordDetails;
    }

    private TrafficLayerRecordVO.TableDetails buildTableDetails(TrafficLayerRecordVO.RecordDetails recordDetails) {
        // Record Table
        PrettyTable recordTable = PrettyTable.fieldNames(RECORD_TABLE_FIELD_NAME);
        Optional.of(recordDetails.getRecord())
                .ifPresent(e -> recordTable.addRow(e.getRecordName(), e.getEnvName(), e.getRouteTrafficTo(), e.getOriginServer()));

        // LB Table
        PrettyTable lbTable = PrettyTable.fieldNames(LB_TABLE_FIELD_NAME);
        Optional.of(recordDetails)
                .map(TrafficLayerRecordVO.RecordDetails::getOriginServer)
                .map(TrafficLayerRecordVO.OriginServer::getOrigins)
                .ifPresent(assets -> assets.forEach(e -> {
                    lbTable.addRow(e.getName(), e.getAssetKey(), e.getAssetType());
                }));

        // Ingress Rule Table
        PrettyTable ingressRuleTable = PrettyTable.fieldNames(RULE_TABLE_FIELD_NAME);
        Optional.of(recordDetails)
                .map(TrafficLayerRecordVO.RecordDetails::getOriginServer)
                .map(TrafficLayerRecordVO.OriginServer::getDetails)
                .ifPresent(stringListMap -> stringListMap.get(RULES)
                        .forEach(e -> ingressRuleTable.addRow(e.getName(), e.getValue())));


        return TrafficLayerRecordVO.TableDetails.builder()
                .recordTable(recordTable.toString())
                .lbTable(lbTable.toString())
                .ingressRuleTable(ingressRuleTable.toString())
                .build();

    }


    @Override
    public String queryRecordDetailsStringTable(TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails) {
        TrafficLayerRecordVO.RecordDetails recordDetails = queryRecordDetails(queryRecordDetails);
        PrettyTable pt = PrettyTable.fieldNames(RULE_TABLE_FIELD_NAME);
        Map<String, List<EdsAssetVO.Index>> originServerDetails = recordDetails.getOriginServer()
                .getDetails();
        originServerDetails.get("RULES")
                .forEach(e -> pt.addRow(e.getName(), e.getValue()));
        return pt.toString();
    }

}
