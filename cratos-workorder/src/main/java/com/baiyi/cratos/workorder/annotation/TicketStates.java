package com.baiyi.cratos.workorder.annotation;

import com.baiyi.cratos.workorder.enums.TicketState;

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

    TicketState state();

    TicketState target() default TicketState.END;

}