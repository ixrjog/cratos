package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.view.robot.RobotVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/18 10:23
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RobotTokenWrapper extends BaseDataTableConverter<RobotVO.RobotToken, Robot> {

    @Override
    public void wrap(RobotVO.RobotToken vo) {
    }

}