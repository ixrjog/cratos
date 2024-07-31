package com.baiyi.cratos.ssh.core.message.audit;

import com.baiyi.cratos.domain.ssh.HasState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/29 下午3:01
 * &#064;Version 1.0
 */
@Data
@JsonIgnoreProperties
public class BaseAuditMessage implements HasState {
    private String state;
    private String sessionId;
    private String instanceId;
}
