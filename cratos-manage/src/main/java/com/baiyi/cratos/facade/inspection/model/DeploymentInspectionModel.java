package com.baiyi.cratos.facade.inspection.model;

import lombok.Builder;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/11 上午11:20
 * &#064;Version 1.0
 */
public class DeploymentInspectionModel {

    @Data
    @Builder
    public static class Deployment {
        String instanceName;
        String name;
    }

}
