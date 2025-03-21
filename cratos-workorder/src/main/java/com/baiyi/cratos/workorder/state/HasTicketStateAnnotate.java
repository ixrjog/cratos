package com.baiyi.cratos.workorder.state;

import com.baiyi.cratos.workorder.annotation.TicketStates;
import org.springframework.aop.support.AopUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 17:49
 * &#064;Version 1.0
 */
public interface HasTicketStateAnnotate extends HasTicketState {

    default TicketState getState() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(TicketStates.class)
                .state();
    }

}
