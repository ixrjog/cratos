package com.baiyi.cratos.eds.zabbix.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.zabbix.enums.SeverityType;
import com.baiyi.cratos.eds.zabbix.request.ZbxEventRequest;
import com.baiyi.cratos.eds.zabbix.result.ZbxEventResult;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxEventService;
import com.baiyi.cratos.eds.zabbix.service.factory.ZbxServiceFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/6 11:40
 * &#064;Version 1.0
 */
public class ZbxEventRepo {

    public static List<ZbxEventResult.Event> listEvent(EdsConfigs.Zabbix zbx) {
        ZbxEventService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxEventService.class);
        Map<String, Object> params = Map.ofEntries(
                entry("output", "extend"),
                entry("selectAcknowledges", "extend"),
                entry("selectSuppressionData", "extend"),
                // entry("suppressed", false)
                entry("sortfield", List.of("clock", "eventid")),
                entry("sortorder", "DESC")
        );
        ZbxEventRequest.GetEvent request = ZbxEventRequest.GetEvent.builder()
                .params(params)
                .build();
        ZbxResponse<List<ZbxEventResult.Event>> response = zbxService.getEvent(request);
        return response.getResult();
    }

    public static List<ZbxEventResult.Event> listEvent(EdsConfigs.Zabbix zbx, Map<String, Object> params) {
        ZbxEventService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxEventService.class);
        ZbxEventRequest.GetEvent request = ZbxEventRequest.GetEvent.builder()
                .params(params)
                .build();
        ZbxResponse<List<ZbxEventResult.Event>> response = zbxService.getEvent(request);
        return response.getResult();
    }

    public static List<ZbxEventResult.Event> listEvent(EdsConfigs.Zabbix zbx, Set<SeverityType> severityTypes, Set<String> eventIds) {
        ZbxEventService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxEventService.class);
        Map<String, Object> params = Map.ofEntries(
                entry("output", "extend"),
                entry("eventids", List.copyOf(eventIds)),
                entry("selectAcknowledges", "extend"),
                entry("selectSuppressionData", "extend"),
                entry("severities", SeverityType.of(severityTypes)),
                // entry("suppressed", false)
                entry("sortfield", List.of("clock", "eventid")),
                entry("sortorder", "DESC")
        );
        ZbxEventRequest.GetEvent request = ZbxEventRequest.GetEvent.builder()
                .params(params)
                .build();
        ZbxResponse<List<ZbxEventResult.Event>> response = zbxService.getEvent(request);
        return response.getResult();
    }

}
