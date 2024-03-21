package com.baiyi.cratos.eds.gitlab.data;

import lombok.Builder;
import lombok.Data;
import org.gitlab4j.api.models.SshKey;

/**
 * @Author baiyi
 * @Date 2024/3/21 15:05
 * @Version 1.0
 */
@Data
@Builder
public class SshKeyData {

    private String username;

    private SshKey sshKey;

}