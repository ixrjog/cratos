package com.baiyi.cratos.facade.inspection.model;

import lombok.Builder;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/12 下午1:41
 * &#064;Version 1.0
 */
public class ApplicationGroupingModel {

    @Data
    @Builder
    public static class ApplicationGrouping {
        String appName;
        String grouping;
    }

}
