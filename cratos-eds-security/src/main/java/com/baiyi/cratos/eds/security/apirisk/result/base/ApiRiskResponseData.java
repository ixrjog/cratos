package com.baiyi.cratos.eds.security.apirisk.result.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/15 14:53
 * &#064;Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRiskResponseData<T> {

    private Long totalCount;
    private List<T> rows;

}
