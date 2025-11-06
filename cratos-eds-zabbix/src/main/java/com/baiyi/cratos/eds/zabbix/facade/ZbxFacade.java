package com.baiyi.cratos.eds.zabbix.facade;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.enums.SeverityType;
import com.baiyi.cratos.eds.zabbix.repo.ZbxEventRepo;
import com.baiyi.cratos.eds.zabbix.repo.ZbxProblemRepo;
import com.baiyi.cratos.eds.zabbix.result.ZbxEventResult;
import com.baiyi.cratos.eds.zabbix.result.ZbxProblemResult;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/4 11:04
 * &#064;Version 1.0
 */
@SuppressWarnings("ALL")
public class ZbxFacade {

    public static List<ZbxProblemResult.Problem> listProblem(EdsZabbixConfigModel.Zabbix zbx) {
        List<Integer> severities = SeverityType.of(zbx);
        Map<String, Object> params = Map.ofEntries(entry("output", "extend"),
                                                   // 排除被抑制的问题
                                                   entry("severities", severities),
                                                   entry("selectSuppressionData", "extend"),
                                                   entry("sortfield", List.of("eventid")),
                                                   entry("recent", true));
        return ZbxProblemRepo.listProblem(zbx, params);
    }

    public static List<ZbxEventResult.Event> listEvent(EdsZabbixConfigModel.Zabbix zbx) {
        List<ZbxProblemResult.Problem> problems = listProblem(zbx);
        if (CollectionUtils.isEmpty(problems)) {
            return List.of();
        }
        List<String> eventids = problems.stream()
                .map(ZbxProblemResult.Problem::getEventid)
                .toList();
        Map<String, Object> params = Map.ofEntries(entry("output", "extend"), entry("selectAcknowledges", "extend"),
                                                   //entry("severityTypes",severityTypes),
                                                   entry("selectSuppressionData", "extend"),
                                                   entry("selectHosts", List.of("hostid", "host", "name")),
                                                   entry("eventids", eventids),
                                                   entry("sortfield", List.of("clock", "eventid")),
                                                   entry("sortorder", "DESC"));
        return ZbxEventRepo.listEvent(zbx, params);
    }

}
