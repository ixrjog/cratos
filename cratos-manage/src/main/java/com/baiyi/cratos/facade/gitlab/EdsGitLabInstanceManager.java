package com.baiyi.cratos.facade.gitlab;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.config.model.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.config.loader.EdsGitLabConfigLoader;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 10:22
 * &#064;Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class EdsGitLabInstanceManager {

    private final EdsInstanceQueryHelper edsInstanceQueryHelper;
    private final EdsGitLabConfigLoader edsGitLabConfigLoader;
    private static final EdsInstanceTypeEnum[] INSTANCE_TYPES = {EdsInstanceTypeEnum.GITLAB};

    public EdsInstance findInstanceByHookToken(String hookToken) {
        return edsInstanceQueryHelper.queryInstance(INSTANCE_TYPES, SysTagKeys.EVENT.getKey())
                .stream()
                .filter(instance -> Optional.ofNullable(edsGitLabConfigLoader.getConfig(instance.getConfigId()))
                        .map(EdsGitLabConfigModel.GitLab::getSystemHooks)
                        .map(EdsGitLabConfigModel.SystemHooks::getToken)
                        .filter(hookToken::equals)
                        .isPresent())
                .findFirst()
                .orElse(null);
    }

}
