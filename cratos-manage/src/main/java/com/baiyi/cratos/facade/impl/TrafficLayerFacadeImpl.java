package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.eds.kubernetes.provider.EdsKubernetesIngressAssetProvider;
import com.baiyi.cratos.facade.TrafficLayerFacade;
import com.baiyi.cratos.facade.proxy.TrafficLayerProxy;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import com.baiyi.cratos.wrapper.TrafficLayerRecordWrapper;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        Optional.of(originServer)
                .map(TrafficLayerRecordVO.OriginServer::getDetails)
                .ifPresent(stringListMap -> {
                    if (stringListMap.containsKey(RULES)) {
                        stringListMap.get(RULES)
                                .forEach(e -> {
                                    if (originServer.getDetails()
                                            .containsKey(EdsKubernetesIngressAssetProvider.SOURCE_IP)) {
                                        String sourceIP = originServer.getDetails()
                                                .get(EdsKubernetesIngressAssetProvider.SOURCE_IP)
                                                .getFirst()
                                                .getValue();
                                        final String sourceIPWrap = Joiner.on("")
                                                .join("[", sourceIP, "]");
                                        ingressRuleTable.addRow(Joiner.on(" ")
                                                .join(e.getName(), sourceIPWrap), e.getValue());
                                    } else {
                                        ingressRuleTable.addRow(e.getName(), e.getValue());
                                    }
                                });
                    }
                });
        return ingressRuleTable.toString();
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
