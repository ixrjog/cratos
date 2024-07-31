package com.baiyi.cratos.eds.gitlab.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gitlab4j.api.models.SshKey;

/**
 * @Author baiyi
 * @Date 2024/3/21 15:05
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SshKeyData {
    private String username;
    private SshKey sshKey;
}