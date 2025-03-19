package com.baiyi.cratos.domain.view.work;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasWorkflow;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.model.I18nModel;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 13:45
 * &#064;Version 1.0
 */
public class WorkOrderVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Menu implements Serializable {
        @Serial
        private static final long serialVersionUID = 883502875092939363L;
        private List<Group> groupList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Group implements I18nModel.HasI18n, HasWorkOrderList, Serializable {
        @Serial
        private static final long serialVersionUID = 883502875092939363L;
        private Integer id;
        private String name;
        private String i18n;
        private Integer seq;
        private String groupType;
        private String icon;
        private String comment;
        private I18nModel.I18nData i18nData;
        private List<WorkOrder> workOrderList;

        @Override
        public Integer getGroupId() {
            return this.id;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @BusinessType(type = BusinessTypeEnum.WORKORDER)
    public static class WorkOrder implements BaseBusiness.IBusinessAnnotate, I18nModel.HasI18n, HasWorkflow, BusinessDocVO.HasBusinessDocs, BusinessTagVO.HasBusinessTags, Serializable {
        @Serial
        private static final long serialVersionUID = 883502875092939363L;
        private Integer id;
        private String name;
        private String i18n;
        private Integer seq;
        private Integer groupId;
        private String status;
        private Boolean valid;
        private String workOrderKey;
        private String color;
        private String docs;
        private String icon;
        private String comment;
        private String workflow;

        private I18nModel.I18nData i18nData;
        private WorkflowModel.Workflow workflowData;

        @Override
        public Integer getBusinessId() {
            return id;
        }
        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;
    }

    public interface HasWorkOrderList {
        Integer getGroupId();

        void setWorkOrderList(List<WorkOrder> workOrderList);
    }

}
