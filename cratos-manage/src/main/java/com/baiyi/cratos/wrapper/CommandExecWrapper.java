package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/18 15:15
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecWrapper extends BaseDataTableConverter<CommandExecVO.CommandExec, CommandExec> implements IBaseWrapper<CommandExecVO.CommandExec> {

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.ENV})
    public void wrap(CommandExecVO.CommandExec vo) {
    }

}
