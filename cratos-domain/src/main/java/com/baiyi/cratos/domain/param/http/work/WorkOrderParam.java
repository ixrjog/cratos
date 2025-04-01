package com.baiyi.cratos.domain.param.http.work;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/1 10:33
 * &#064;Version 1.0
 */
public class WorkOrderParam {

    @Data
    @Schema
    public static class UpdateWorkOrder implements IToTarget<WorkOrder> {
        private Integer id;
        private String name;
        private Integer seq;
        private String workOrderKey;
        private Integer groupId;
        private String status;
        private String icon;
        private Boolean valid;
        private String color;
        private String docs;
        private String comment;
        private String i18n;
        private String workflow;
        private String version;
    }

}
