package com.baiyi.cratos.eds;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.auth.ZbxTokenHolder;
import com.baiyi.cratos.eds.zabbix.enums.SeverityType;
import com.baiyi.cratos.eds.zabbix.facade.ZbxFacade;
import com.baiyi.cratos.eds.zabbix.repo.*;
import com.baiyi.cratos.eds.zabbix.result.*;
import com.baiyi.cratos.eds.zabbix.util.ZbxInfoUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 11:18
 * &#064;Version 1.0
 */
public class EdsZbxTest extends BaseEdsTest<EdsZabbixConfigModel.Zabbix> {

    @Resource
    private ZbxTokenHolder zbxTokenHolder;

    @Test
    void test1() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        String token = zbxTokenHolder.getToken(zbx);
        System.out.println(token);
    }

    @Test
    void test2() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        List<ZbxHostResult.Host> hosts = ZbxHostRepo.listHost(zbx);
        System.out.println(hosts);
    }

    @Test
    void test3() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        List<ZbxTemplateResult.Template> templates = ZbxTemplateRepo.listTemplate(zbx);
        System.out.println(templates);
    }

    @Test
    void test4() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        String version = ZbxInfoUtils.getVersion(zbx);
        System.out.println(version);
    }

    @Test
    void test5() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        List<ZbxHostGroupResult.HostGroup> groups = ZbxHostGroupRepo.listHostGroup(zbx);
        System.out.println(groups);
    }

    @Test
    void test7() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        ZbxHostResult.HostExtend hostExtend = ZbxHostRepo.getHostExtend(zbx, "10710");
        System.out.println(hostExtend);
    }

    @Test
    void test8() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        Set<SeverityType> severityTypes = Set.of(SeverityType.AVERAGE);
        List<ZbxProblemResult.Problem> problems = ZbxProblemRepo.listProblem(zbx, severityTypes);
        printProblems(problems);
    }

    void printProblems(List<ZbxProblemResult.Problem> problems) {
        for (ZbxProblemResult.Problem problem : problems) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            java.lang.reflect.Method[] methods = problem.getClass()
                    .getMethods();
            boolean first = true;
            for (java.lang.reflect.Method m : methods) {
                if (java.lang.reflect.Modifier.isPublic(m.getModifiers()) && m.getParameterCount() == 0 && m.getName()
                        .startsWith("get") && !m.getName()
                        .equals("getClass")) {
                    try {
                        Object val = m.invoke(problem);
                        String name = m.getName()
                                .substring(3);
                        name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                        if (!first) sb.append(", ");
                        sb.append(name)
                                .append("=")
                                .append(val);
                        first = false;
                    } catch (Exception ignored) {
                    }
                }
            }
            sb.append("}");
            System.out.println(sb.toString());
        }
    }

    @Test
    void test10() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        Set<SeverityType> severityTypes = Set.of(SeverityType.WARNING, SeverityType.AVERAGE, SeverityType.HIGH);
        List<ZbxEventResult.Event> events = ZbxEventRepo.listEvent(zbx);

        List<ZbxEventResult.Event> events2 = events.stream()
                .filter(event -> !event.getREventid()
                        .equals("0"))
                .toList();

        System.out.println(events2);
    }

    @Test
    void test11() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        Set<SeverityType> severityTypes = Set.of(SeverityType.AVERAGE, SeverityType.HIGH);
        List<ZbxEventResult.Event> events = ZbxFacade.listEvent(zbx);
        // System.out.println(events);
        for (ZbxEventResult.Event event : events) {
            List<ZbxHostResult.Host> hosts = Optional.of(event)
                    .map(ZbxEventResult.Event::getHosts)
                    .orElse(List.of());
            if (CollectionUtils.isEmpty(hosts)) {
                System.out.println(event.getName() + " no host");
                continue;
            }
            ZbxHostResult.Host host = hosts.getFirst();
            System.out.println(
                    StringFormatter.arrayFormat(
                            "EventID {} Host {} -> {}", event.getEventid(), host.getHost(), event.getName()));
        }
    }

    @Test
    void test12() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        List<ZbxUserResult.User> users = ZbxUserRepo.listUser(zbx);
        System.out.println(users);
    }


}

