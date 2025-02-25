package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import com.baiyi.cratos.facade.command.CommandExecFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/18 10:29
 * &#064;Version 1.0
 */
public class CommandExecTest extends BaseUnit {

    @Resource
    private CommandExecFacade commandExecFacade;

    @Test
    void test1() {
        SessionUtils.setUsername("xiuyuan");
        CommandExecParam.ExecTarget execTarget = CommandExecParam.ExecTarget.builder()
                .instanceId(101)
                .namespace("prod")
                .build();
        CommandExecParam.AddCommandExec addCommandExec = CommandExecParam.AddCommandExec.builder()
                .autoExec(true)
                .applyRemark("测试一下")
                .command("curl -I https://www.baidu.com")
                .approvedBy("baiyi")
                .execTarget(execTarget )
                .build();
        commandExecFacade.addCommandExec(addCommandExec);
    }

}
