package com.baiyi.cratos.domain.view.access;

import com.baiyi.cratos.domain.BaseBusiness;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/13 16:01
 * &#064;Version 1.0
 */
@SuppressWarnings("rawtypes")
public class AccessControlVO {

    public enum OperationPermission {
        DEPLOYMENT_POD_DELETE,
        DEPLOYMENT_REDEPLOY
    }

    public interface HasAccessControl extends BaseBusiness.IBusinessAnnotate {

        void setAccessControl(AccessControl accessControl);

        String getNamespace();

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AccessControl<T> implements Serializable {
        @Serial
        private static final long serialVersionUID = -117053221590362418L;
        private Boolean permission;
        private String businessType;
        private String msg;
        // 操作权限
        private Map<String, T> operationPermissions;

        public static AccessControl unauthorized(String businessType) {
            return AccessControl.builder()
                    .permission(false)
                    .businessType(businessType)
                    .operationPermissions(Maps.newHashMap())
                    .build();
        }

        public static AccessControl unauthorized(String businessType, String msg) {
            return AccessControl.builder()
                    .permission(false)
                    .businessType(businessType)
                    .msg(msg)
                    .operationPermissions(Maps.newHashMap())
                    .build();
        }

        public static AccessControl authorized(String businessType) {
            return AccessControl.builder()
                    .permission(true)
                    .businessType(businessType)
                    .operationPermissions(Maps.newHashMap())
                    .build();
        }
    }

}
