package com.baiyi.cratos.domain.view.ssh;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.HasResourceCount;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午11:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SshInstanceVO {

    public interface HasSessionInstances {
        String getSessionId();
        void setSessionInstances(List<Instance> instances);
        List<Instance> getSessionInstances();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.SSH_INSTANCE)
    public static class Instance extends BaseVO implements HasResourceCount, Serializable {
        @Serial
        private static final long serialVersionUID = -6573176155485274089L;
        private Integer id;
        private String sessionId;
        private String instanceId;
        private String duplicateInstanceId;
        private String instanceType;
        private String loginUser;
        private String destIp;
        private Long outputSize;
        private Boolean instanceClosed;
        private String auditPath;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date startTime;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date endTime;
        @Schema(description = "Resource Count")
        private Map<String, Integer> resourceCount;
    }

}
