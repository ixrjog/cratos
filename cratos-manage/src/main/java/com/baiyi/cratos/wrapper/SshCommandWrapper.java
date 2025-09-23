package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.SshSessionInstanceCommand;
import com.baiyi.cratos.domain.view.ssh.SshCommandVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 下午2:19
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SshCommandWrapper extends BaseDataTableConverter<SshCommandVO.Command, SshSessionInstanceCommand> implements BaseWrapper<SshCommandVO.Command> {

    @Override
    public void wrap(SshCommandVO.Command vo) {
    }

}