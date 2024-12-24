package com.baiyi.cratos.my;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.ApplicationActuator;
import com.baiyi.cratos.facade.application.model.ApplicationActuatorModel;
import com.baiyi.cratos.service.ApplicationActuatorService;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/24 11:14
 * &#064;Version 1.0
 */
@Slf4j
public class ApplicationActuatorTest extends BaseUnit {

    @Resource
    private ApplicationActuatorService applicationActuatorService;

    @Test
    void test() {
        Map<String, Integer> cntMap = Maps.newHashMap();
        List<ApplicationActuator> applicationActuators = applicationActuatorService.selectAll();
        applicationActuators.forEach(applicationActuator -> {
            try {
                ApplicationActuatorModel.Lifecycle lifecycle = ApplicationActuatorModel.lifecycleLoadAs(
                        applicationActuator);
                String cmd = Joiner.on(" ")
                        .skipNulls()
                        .join(lifecycle.getPreStop()
                                .getExec()
                                .getCommand());
                String msg = "app={} env={} lifecycle.preStop cmd: {}";
                System.out.println(StringFormatter.arrayFormat(msg, applicationActuator.getApplicationName(),
                        applicationActuator.getNamespace(), cmd));
                putMap(cntMap, cmd);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        System.out.println(cntMap);
    }

    private void putMap(Map<String, Integer> cntMap, String cmd) {
        if (cntMap.containsKey(cmd)) {
            cntMap.put(cmd, cntMap.get(cmd) + 1);
        } else {
            cntMap.put(cmd, 1);
        }
    }

}
