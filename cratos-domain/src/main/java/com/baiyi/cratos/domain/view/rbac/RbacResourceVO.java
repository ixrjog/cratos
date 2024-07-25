package com.baiyi.cratos.domain.view.rbac;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/18 10:01
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RbacResourceVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Resource extends BaseVO implements RbacGroupVO.HasRbacGroup {
        @Serial
        private static final long serialVersionUID = 1537978025458286103L;
        private Integer id;
        private Integer groupId;
        private String resourceName;
        private String comment;
        private Boolean valid;
        private Boolean uiPoint;
        private RbacGroupVO.Group rbacGroup;
        @Override
        public Integer getRbacGroupId() {
            return groupId;
        }
    }

}
