package com.baiyi.cratos.facade.eaglecloud;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.config.model.EdsEagleCloudConfigModel;
import com.baiyi.cratos.eds.core.config.loader.EagleCloudSaseConfigLoader;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 15:33
 * &#064;Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class EdsEagleCloudSaseInstanceManager {

    private final EdsInstanceQueryHelper edsInstanceQueryHelper;
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
        return edsInstanceQueryHelper.queryInstance(INSTANCE_TYPES, SysTagKeys.EVENT.getKey())
                .stream()
                .filter(instance -> {
                    EdsEagleCloudConfigModel.Sase sase = eagleCloudSaseConfigLoader.getConfig(instance.getConfigId());
                    return sase != null && token.equals(sase.getCred()
                                                                .getToken());
                })
                .findFirst()
                .orElse(null);
    }

}
