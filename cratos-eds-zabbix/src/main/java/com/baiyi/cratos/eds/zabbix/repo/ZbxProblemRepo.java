package com.baiyi.cratos.eds.zabbix.repo;

import com.baiyi.cratos.eds.core.config.model.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.constant.ZbxParamConstants;
import com.baiyi.cratos.eds.zabbix.enums.SeverityType;
import com.baiyi.cratos.eds.zabbix.request.ZbxProblemRequest;
import com.baiyi.cratos.eds.zabbix.result.ZbxProblemResult;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxProblemService;
import com.baiyi.cratos.eds.zabbix.service.factory.ZbxServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/3 17:10
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxProblemRepo {

    public static List<ZbxProblemResult.Problem> listProblem(EdsZabbixConfigModel.Zabbix zbx,
                                                             Set<SeverityType> severityTypes) {
        ZbxProblemService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxProblemService.class);
        Map<String, Object> params = Map.ofEntries(entry("output", "extend"), entry("selectAcknowledges", "extend"),
                entry("severities", severityTypes.stream()
                        .map(SeverityType::getType)
                        .collect(Collectors.toList())), entry("recent", true));
        ZbxProblemRequest.GetProblem request = ZbxProblemRequest.GetProblem.builder()
                .params(params)
                .build();
        ZbxResponse<List<ZbxProblemResult.Problem>> response = zbxService.getProblem(request);
        return response.getResult();
    }

    /**
     * 在“问题”仪表板上，Zabbix 仅显示活动主机的问题。因此需要获取所需活动主机的列表查询
     *
     * @param zbx
     * @param severityTypes
     * @param hostIds
     * @return
     */
    public static List<ZbxProblemResult.Problem> listProblem(EdsZabbixConfigModel.Zabbix zbx,
                                                             Set<SeverityType> severityTypes, Set<String> hostIds) {
        ZbxProblemService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxProblemService.class);

        Map<String, Object> params = Map.ofEntries(ZbxParamConstants.OUTPUT_EXTEND,
                ZbxParamConstants.SELECT_ACKNOWLEDGES_EXTEND,
                entry("selectHosts", List.of("hostid", "host", "name")),
                entry("selectTags", "extend"),
                // 排除被抑制的问题
                entry("selectSuppressionData", "extend"),
                entry("sortfield", List.of("eventid")),
                // 只查询活动主机的问题
                // entry("hostids", List.copyOf(hostIds)),
                ZbxParamConstants.SORT_ORDER_DESC, ZbxParamConstants.RECENT);
        ZbxProblemRequest.GetProblem request = ZbxProblemRequest.GetProblem.builder()
                .params(params)
                .build();
        ZbxResponse<List<ZbxProblemResult.Problem>> response = zbxService.getProblem(request);
        return response.getResult();
    }

    public static List<ZbxProblemResult.Problem> listProblem(EdsZabbixConfigModel.Zabbix zbx,
                                                             Map<String, Object> params) {
        ZbxProblemService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxProblemService.class);
        ZbxProblemRequest.GetProblem request = ZbxProblemRequest.GetProblem.builder()
                .params(params)
                .build();
        ZbxResponse<List<ZbxProblemResult.Problem>> response = zbxService.getProblem(request);
        return response.getResult();
    }

    public static String listProblemTest(EdsZabbixConfigModel.Zabbix zbx, Set<SeverityType> severityTypes) {
        ZbxProblemService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxProblemService.class);
        Map<String, Object> params = Map.ofEntries(entry("output", "extend"),
                entry("severities", SeverityType.of(severityTypes)), entry("recent", true));
        ZbxProblemRequest.GetProblem request = ZbxProblemRequest.GetProblem.builder()
                .params(params)
                .build();
        return zbxService.getProblemTest(request);
    }

}
