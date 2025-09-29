package com.baiyi.cratos.facade.eaglecloud;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.config.EdsEagleCloudConfigModel;
import com.baiyi.cratos.eds.core.config.loader.EagleCloudSaseConfigLoader;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 15:33
 * &#064;Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class EdsEagleCloudSaseInstanceManager {

    private final EdsInstanceHelper edsInstanceHelper;
    private final EagleCloudSaseConfigLoader eagleCloudSaseConfigLoader;
    private static final EdsInstanceTypeEnum[] INSTANCE_TYPES = {EdsInstanceTypeEnum.EAGLECLOUD_SASE};

    public EdsInstance findInstanceByHookToken(String hookToken) {
        if (!StringUtils.hasText(hookToken) || !hookToken.startsWith("Token ")) {
            return null;
        }
        String token = hookToken.substring(6)
                .trim();
        if (!StringUtils.hasText(token)) {
            return null;
        }
        return edsInstanceHelper.queryInstance(INSTANCE_TYPES, SysTagKeys.EVENT.getKey())
                .stream()
                .filter(instance -> {
                    EdsEagleCloudConfigModel.Sase sase = Optional.ofNullable(
                                    eagleCloudSaseConfigLoader.getConfig(instance.getConfigId()))
                            .map(EdsEagleCloudConfigModel.Sase.class::cast)
                            .orElse(null);
                    return sase != null && token.equals(sase.getCred()
                            .getToken());
                })
                .findFirst()
                .orElse(null);
    }

}
