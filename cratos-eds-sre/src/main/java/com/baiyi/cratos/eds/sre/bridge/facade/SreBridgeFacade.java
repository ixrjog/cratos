package com.baiyi.cratos.eds.sre.bridge.facade;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.domain.model.SreBridgeModel;
import com.baiyi.cratos.eds.sre.bridge.service.SreBridgeService;
import com.baiyi.cratos.eds.sre.bridge.service.SreBridgeServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 16:04
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SreBridgeFacade {

    public static SreBridgeModel.Result collectEvent(EdsConfigs.SreEventBridge sreEventBridge,
                                                     SreBridgeModel.Event event) {
        SreBridgeService sreBridgeService = SreBridgeServiceFactory.createSreBridgeService(sreEventBridge);
        return sreBridgeService.collectEvent(event);
    }

}
