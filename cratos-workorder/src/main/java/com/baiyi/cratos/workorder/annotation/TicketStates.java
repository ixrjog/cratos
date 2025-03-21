package com.baiyi.cratos.workorder.annotation;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 17:43
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TicketStates {

    com.baiyi.cratos.workorder.state.TicketState state();

}