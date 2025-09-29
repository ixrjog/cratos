package com.baiyi.cratos.facade.eaglecloud.impl;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.event.EagleCloudEventParam;
import com.baiyi.cratos.eds.core.config.EdsEagleCloudConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.eaglecloud.EagleCloudSaseFacade;
import com.baiyi.cratos.facade.eaglecloud.EdsEagleCloudSaseInstanceManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 15:04
 * &#064;Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class EagleCloudSaseFacadeImpl implements EagleCloudSaseFacade {

    private final EdsEagleCloudSaseInstanceManager eagleCloudSaseInstanceManager;
    private final EdsInstanceProviderHolderBuilder holderBuilder;

    @SuppressWarnings("unchecked")
    @Override
    public void consumeEvent(EagleCloudEventParam.SaseHook saseHook, String hookToken) {
        if (StringUtils.isAnyBlank(saseHook.getContent(), hookToken)) {
            log.debug("Invalid event: missing event content or hook token.");
            return;
        }
        EdsInstance edsInstance = eagleCloudSaseInstanceManager.findInstanceByHookToken(hookToken);
        if (Objects.isNull(edsInstance)) {
            log.debug("No matching instance found for the provided hook token.");
            return;
        }
        EdsInstanceProviderHolder<EdsEagleCloudConfigModel.Sase, EagleCloudEventParam.SaseHook> holder = (EdsInstanceProviderHolder<EdsEagleCloudConfigModel.Sase, EagleCloudEventParam.SaseHook>) holderBuilder.newHolder(
                edsInstance.getId(), EdsAssetTypeEnum.EAGLECLOUD_SASE_DATA_SECURITY_EVENT.name());
        if (holder != null) {
            holder.importAsset(saseHook);
        }
    }

}
