package com.baiyi.cratos.workorder.state;

import com.baiyi.cratos.workorder.enums.TicketState;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 17:47
 * &#064;Version 1.0
 */
public interface HasTicketState {

    TicketState getState();

}
