package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.auth.ZbxTokenHolder;
import com.baiyi.cratos.eds.zabbix.repo.ZbxHostGroupRepo;
import com.baiyi.cratos.eds.zabbix.repo.ZbxHostRepo;
import com.baiyi.cratos.eds.zabbix.repo.ZbxTemplateRepo;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostGroupResult;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostResult;
import com.baiyi.cratos.eds.zabbix.result.ZbxTemplateResult;
import com.baiyi.cratos.eds.zabbix.util.ZbxInfoUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 11:18
 * &#064;Version 1.0
 */
public class EdsZbxTest extends BaseEdsTest<EdsZabbixConfigModel.Zabbix> {

    @Resource
    private ZbxTokenHolder zbxTokenHolder;

    @Resource
    private ZbxHostRepo zbxHostRepo;

    @Resource
    private ZbxTemplateRepo zbxTemplateRepo;

    @Resource
    private ZbxHostGroupRepo zbxHostGroupRepo;

    @Test
    void test1() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        String token = zbxTokenHolder.getToken(zbx);
        System.out.println(token);
    }

    @Test
    void test2() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        List<ZbxHostResult.Host> hosts = zbxHostRepo.listHost(zbx);
        System.out.println(hosts);
    }

    @Test
    void test3() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        List<ZbxTemplateResult.Template> templates = zbxTemplateRepo.listTemplate(zbx);
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
        List<ZbxHostGroupResult.HostGroup> groups = zbxHostGroupRepo.listHostGroup(zbx);
        System.out.println(groups);
    }

    @Test
    void test7() {
        EdsZabbixConfigModel.Zabbix zbx = getConfig(49);
        ZbxHostResult.HostExtend hostExtend = zbxHostRepo.getHostExtend(zbx, "10710");
        System.out.println(hostExtend);
    }

}

