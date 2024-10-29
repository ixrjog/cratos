package com.baiyi.cratos.domain.view.ssh;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.HasDurationTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午11:19
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SshSessionVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.SSH_SESSION)
    public static class Session extends BaseVO implements SshInstanceVO.HasSessionInstances, HasDurationTime, Serializable {
        @Serial
        private static final long serialVersionUID = -5432624545362080316L;
        private Integer id;
        private String sessionId;
        private String username;
        private String remoteAddr;
        private String sessionStatus;
        private String serverHostname;
        private String serverAddr;
        private String sessionType;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date startTime;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date endTime;
        private List<SshInstanceVO.Instance> sessionInstances;
        private String durationTime;
    }

}
