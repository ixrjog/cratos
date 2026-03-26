package com.baiyi.cratos.facade.inspection.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/26 10:12
 * &#064;Version 1.0
 */
public class GoogleCloudModel {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class IamMember {
        String instanceName;
        String member;
    }

}
