package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import com.baiyi.cratos.facade.CommandExecFacade;
import com.baiyi.cratos.service.CommandExecService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 15:48
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecFacadeImpl implements CommandExecFacade {

    private final CommandExecService commandExecService;

    @Override
    public void addCommandExec(CommandExecParam.AddCommandExec addCommandExec) {
        CommandExec commandExec = addCommandExec.toTarget();
    }
}
