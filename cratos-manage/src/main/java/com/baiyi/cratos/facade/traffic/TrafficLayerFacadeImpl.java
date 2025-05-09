package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.facade.TrafficLayerFacade;
import com.baiyi.cratos.facade.proxy.TrafficLayerProxy;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import com.baiyi.cratos.wrapper.TrafficLayerRecordWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_INGRESS_ORDER;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_INGRESS_SOURCE_IP;
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
    private final EdsAssetIndexService edsAssetIndexService;

    public final static String[] RECORD_TABLE_FIELD_NAME = {"Record Name", "Env Name", "Route Traffic To", "Origin Server"};
    public final static String[] LB_TABLE_FIELD_NAME = {"Load Balancer Name", "DNS Name", "Load Balancer Provider"};
    public final static String[] RULE_TABLE_FIELD_NAME = {"Ingress Rule", "Source IP", "Order", "Kubernetes Service"};

    @Override
    public TrafficLayerRecordVO.RecordDetails queryRecordDetails(
            TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails) {
        TrafficLayerDomainRecord uniqueKey = queryRecordDetails.toTrafficLayerRecord();
        TrafficLayerDomainRecord trafficLayerDomainRecord = recordService.getByUniqueKey(uniqueKey);
        if (trafficLayerDomainRecord == null) {
            return TrafficLayerRecordVO.RecordDetails.NOT_FOUND;

        }
        TrafficLayerRecordVO.Record record = recordWrapper.wrapToTarget(trafficLayerDomainRecord);
        TrafficLayerRecordVO.OriginServer originServer = trafficLayerProxy.buildOriginServer(
                trafficLayerDomainRecord.getRecordName(), trafficLayerDomainRecord.getOriginServer());
        return TrafficLayerRecordVO.RecordDetails.builder()
                .recordId(trafficLayerDomainRecord.getId())
                .record(record)
                .originServer(originServer)
                .tableDetails(buildTableDetails(record, originServer))
                .build();
    }

    private TrafficLayerRecordVO.TableDetails buildTableDetails(TrafficLayerRecordVO.Record record,
                                                                TrafficLayerRecordVO.OriginServer originServer) {
        return TrafficLayerRecordVO.TableDetails.builder()
                .recordTable(toRecordTableStr(record))
                .lbTable(toLbTableStr(originServer))
                .ingressRuleTable(toIngressRuleTableStr(originServer))
                .build();
    }

    private String toIngressRuleTableStr(TrafficLayerRecordVO.OriginServer originServer) {
        PrettyTable ingressRuleTable = PrettyTable.fieldNames(RULE_TABLE_FIELD_NAME);
        Optional.ofNullable(originServer)
                .map(TrafficLayerRecordVO.OriginServer::getDetails)
                .map(details -> details.get(RULES))
                .ifPresent(rules -> rules.forEach(e -> {
                    String sourceIP = getRuleSourceIp(e);
                    String order = getRuleOrder(e);
                    ingressRuleTable.addRow(e.getName(), sourceIP, order, e.getValue());
                }));
        return ingressRuleTable.toString();
    }

    private String getRuleSourceIp(EdsAssetVO.Index ruleIndex) {
        EdsAssetIndex uk = EdsAssetIndex.builder()
                .instanceId(ruleIndex.getInstanceId())
                .assetId(ruleIndex.getAssetId())
                .name(KUBERNETES_INGRESS_SOURCE_IP)
                .build();
        EdsAssetIndex sourceIPIndex = edsAssetIndexService.getByUniqueKey(uk);
        return sourceIPIndex == null ? "-" : "[" + sourceIPIndex.getValue() + "]";
    }

    private String getRuleOrder(EdsAssetVO.Index ruleIndex) {
        EdsAssetIndex uk = EdsAssetIndex.builder()
                .instanceId(ruleIndex.getInstanceId())
                .assetId(ruleIndex.getAssetId())
                .name(KUBERNETES_INGRESS_ORDER)
                .build();
        EdsAssetIndex orderIndex = edsAssetIndexService.getByUniqueKey(uk);
        return orderIndex == null ? "-" : orderIndex.getValue();
    }

    private String toLbTableStr(TrafficLayerRecordVO.OriginServer originServer) {
        PrettyTable lbTable = PrettyTable.fieldNames(LB_TABLE_FIELD_NAME);
        Optional.of(originServer)
                .map(TrafficLayerRecordVO.OriginServer::getOrigins)
                .ifPresent(
                        assets -> assets.forEach(e -> lbTable.addRow(e.getName(), e.getAssetKey(), e.getAssetType())));
        return lbTable.toString();
    }

    private String toRecordTableStr(TrafficLayerRecordVO.Record record) {
        // Record Table
        PrettyTable recordTable = PrettyTable.fieldNames(RECORD_TABLE_FIELD_NAME);
        Optional.of(record)
                .ifPresent(e -> recordTable.addRow(e.getRecordName(), e.getEnvName(), e.getRouteTrafficTo(),
                        e.getOriginServer()));
        return recordTable.toString();
    }

}
