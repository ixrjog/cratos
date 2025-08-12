package com.baiyi.cratos.ssh.crystal.annotation;

import com.baiyi.cratos.ssh.core.enums.MessageState;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/12 10:33
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MessageStates {

    MessageState state();

}