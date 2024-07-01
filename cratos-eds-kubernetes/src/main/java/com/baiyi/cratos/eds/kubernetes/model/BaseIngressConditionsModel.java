package com.baiyi.cratos.eds.kubernetes.model;

import lombok.Data;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/27 下午5:42
 * &#064;Version 1.0
 */
public class BaseIngressConditionsModel {

    public interface HasSourceIpConfig {

        SourceIpConfig getSourceIpConfig();

    }

    @Data
    public static class SourceIpConfig {

        private List<String> values;

    }

}
