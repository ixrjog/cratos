package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.auth.ZbxTokenHolder;
import com.baiyi.cratos.eds.zabbix.enums.SeverityType;
import com.baiyi.cratos.eds.zabbix.facade.ZbxProblemFacade;
import com.baiyi.cratos.eds.zabbix.repo.ZbxHostGroupRepo;
import com.baiyi.cratos.eds.zabbix.repo.ZbxHostRepo;
import com.baiyi.cratos.eds.zabbix.repo.ZbxProblemRepo;
import com.baiyi.cratos.eds.zabbix.repo.ZbxTemplateRepo;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostGroupResult;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostResult;
import com.baiyi.cratos.eds.zabbix.result.ZbxProblemResult;
import com.baiyi.cratos.eds.zabbix.result.ZbxTemplateResult;
import com.baiyi.cratos.eds.zabbix.util.ZbxInfoUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
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
        //System.out.println(problems);
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
    void test9() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        Set<SeverityType> severityTypes = Set.of(SeverityType.WARNING, SeverityType.AVERAGE);
        List<ZbxProblemResult.Problem>  problems = ZbxProblemFacade.listProblem(zbx, severityTypes);
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

}

