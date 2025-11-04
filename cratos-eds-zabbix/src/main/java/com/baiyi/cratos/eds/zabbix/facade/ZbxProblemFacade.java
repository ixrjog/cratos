package com.baiyi.cratos.eds.zabbix.facade;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.enums.SeverityType;
import com.baiyi.cratos.eds.zabbix.repo.ZbxHostRepo;
import com.baiyi.cratos.eds.zabbix.repo.ZbxProblemRepo;
import com.baiyi.cratos.eds.zabbix.result.ZbxProblemResult;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/4 11:04
 * &#064;Version 1.0
 */
public class ZbxProblemFacade {

    public static List<ZbxProblemResult.Problem> listProblem(EdsZabbixConfigModel.Zabbix zbx,
                                                             Set<SeverityType> severityTypes) {
        Map<String, Object> params = Map.ofEntries(entry("output", List.of()), entry("preservekeys", true),
                entry("filter", Map.of("status", 1)));
        List<String> hostids = ZbxHostRepo.listHostIds(zbx, params);
        if (CollectionUtils.isEmpty(hostids)) {
            return List.of();
        }
        return ZbxProblemRepo.listProblem(zbx, severityTypes, Set.copyOf(hostids));
    }

}
