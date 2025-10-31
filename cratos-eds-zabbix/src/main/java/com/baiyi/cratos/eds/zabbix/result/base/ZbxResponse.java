package com.baiyi.cratos.eds.zabbix.result.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 10:21
 * &#064;Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZbxResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2135779601525149910L;
    private String jsonrpc;
    private T result;

}
