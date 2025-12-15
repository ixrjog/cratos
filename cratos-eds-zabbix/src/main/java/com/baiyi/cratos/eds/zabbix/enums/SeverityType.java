package com.baiyi.cratos.eds.zabbix.enums;

import com.baiyi.cratos.eds.core.config.model.EdsZabbixConfigModel;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author baiyi
 * @Date 2021/2/18 10:33 上午
 * @Version 1.0
 */
@Getter
public enum SeverityType {

    /**
     * Zabbix
     * Severity of the trigger.
     * Possible values are:
     * 0 - (default) not classified;
     * 1 - information;
     * 2 - warning;
     * 3 - average;
     * 4 - high;
     * 5 - disaster.
     */

    DEFAULT(0, "DEFAULT"),
    INFORMATION(1, "INFORMATION"),
    WARNING(2, "WARNING"),
    AVERAGE(3, "AVERAGE"),
    HIGH(4, "HIGH"),
    DISASTER(5, "DISASTER");

    private final int type;

    private final String name;

    SeverityType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(int type) {
        return Arrays.stream(SeverityType.values())
                .filter(severityType -> severityType.getType() == type)
                .findFirst()
                .map(SeverityType::getName)
                .orElse("DEFAULT");
    }

    public static SeverityType getByName(String name) {
        return Arrays.stream(SeverityType.values())
                .filter(severityType -> severityType.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public static List<Integer> of(EdsZabbixConfigModel.Zabbix zbx) {
        List<String> severityTypes = Optional.ofNullable(zbx)
                .map(EdsZabbixConfigModel.Zabbix::getSeverityTypes)
                .orElse(List.of());
        if (CollectionUtils.isEmpty(severityTypes)) {
            severityTypes = Arrays.stream(SeverityType.values())
                    .map(SeverityType::getName)
                    .toList();
        }
        return severityTypes.stream()
                .map(SeverityType::getByName)
                .filter(Objects::nonNull)
                .map(SeverityType::getType)
                .toList();
    }

    public static List<Integer> of(Set<SeverityType> severityTypes) {
        return severityTypes.stream()
                .map(SeverityType::getType)
                .toList();
    }


}