package com.baiyi.cratos.eds.core.util;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum.SRE_EVENTBRIDGE_EVENT;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 16:55
 * &#064;Version 1.0
 */
@Slf4j
@Component
@SuppressWarnings("unchecked")
public class SreBridgeUtils {

    private static EdsInstanceService edsInstanceService;
    private static EdsProviderHolderFactory edsProviderHolderFactory;

    @Autowired
    public void setEdsInstanceService(EdsInstanceService edsInstanceService) {
        setService(edsInstanceService);
    }

    @Autowired
    public void setEdsProviderHolderFactory(EdsProviderHolderFactory edsProviderHolderFactory) {
        setService(edsProviderHolderFactory);
    }

    private static void setService(EdsInstanceService edsInstanceService) {
        SreBridgeUtils.edsInstanceService = edsInstanceService;
    }

    private static void setService(EdsProviderHolderFactory edsProviderHolderFactory) {
        SreBridgeUtils.edsProviderHolderFactory = edsProviderHolderFactory;
    }

    /**
     * 发布事件到所有实例
     *
     * @param event
     */
    public static void publish(com.baiyi.cratos.domain.model.SreBridgeModel.Event event) {
        try {
            List<EdsInstance> instances = edsInstanceService.queryValidEdsInstanceByType(
                    EdsInstanceTypeEnum.SRE_EVENTBRIDGE.name());
            if (CollectionUtils.isEmpty(instances)) {
                return;
            }
            for (EdsInstance instance : instances) {
                try {
                    EdsInstanceProviderHolder<EdsConfigs.SreEventBridge, com.baiyi.cratos.domain.model.SreBridgeModel.Event> holder = (EdsInstanceProviderHolder<EdsConfigs.SreEventBridge, com.baiyi.cratos.domain.model.SreBridgeModel.Event>) edsProviderHolderFactory.createHolder(
                            instance.getId(), SRE_EVENTBRIDGE_EVENT.name());
                    holder.importAsset(event);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
