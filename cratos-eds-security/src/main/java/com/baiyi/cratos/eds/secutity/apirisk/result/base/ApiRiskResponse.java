package com.baiyi.cratos.eds.secutity.apirisk.result.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/15 14:51
 * &#064;Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRiskResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -3080619270482542353L;
    private String msg;
    private Integer errorCode;
    private ApiRiskResponseData<T> data;

}