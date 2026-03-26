package com.baiyi.cratos.facade.inspection.context;

import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/26 10:42
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class InspectionTaskContext {

    private final NotificationTemplateService notificationTemplateService;
    private final DingtalkService dingtalkService;
    private final EdsInstanceQueryHelper edsInstanceQueryHelper;
    private final EdsConfigService edsConfigService;

}
