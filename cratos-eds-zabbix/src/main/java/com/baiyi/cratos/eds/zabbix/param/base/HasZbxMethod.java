package com.baiyi.cratos.eds.zabbix.param.base;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 14:49
 * &#064;Version 1.0
 */
@Data
@SuperBuilder(toBuilder = true)
public class HasZbxMethod implements BaseZbxParam.HasMethodAnnotate {

    private String method;

    public String getMethod() {
        if (this.method == null) {
            return acqMethod();
        }
        return this.method;
    }

}
