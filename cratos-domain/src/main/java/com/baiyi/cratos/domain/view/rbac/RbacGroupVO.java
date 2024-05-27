package com.baiyi.cratos.domain.view.rbac;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.IResourceCount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/1/18 10:01
 * @Version 1.0
 */
public class RbacGroupVO {

    public interface HasRbacGroup {
        Integer getRbacGroupId();

        void setRbacGroup(Group rbacGroup);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Group extends BaseVO implements IResourceCount {

        @Serial
        private static final long serialVersionUID = 7404729006872174030L;

        private Integer id;

        private String groupName;

        private String base;

        private String comment;

        @Schema(description = "Resource Count")
        private Map<String, Integer> resourceCount;

    }

}
