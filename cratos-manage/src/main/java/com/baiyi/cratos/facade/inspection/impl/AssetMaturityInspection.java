package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.AssetMaturity;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.facade.inspection.base.BaseInspection;
import com.baiyi.cratos.service.AssetMaturityService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.CUSTOM_ASSET_EXPIRATION_INSPECTION_NOTIFICATION;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/30 下午5:40
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class AssetMaturityInspection extends BaseInspection {

    private final AssetMaturityService assetMaturityService;

    private static final int ASSET_MATURITY_EXPIRY_DAYS = 30;
    private static final String ASSET_MATURITIES_FIELD = "assetMaturities";
    private static final String EXPIRY_DAYS_FIELD = "expiryDays";


    public AssetMaturityInspection(NotificationTemplateService notificationTemplateService,
                                   DingtalkService dingtalkService, EdsInstanceHelper edsInstanceHelper,
                                   EdsConfigService edsConfigService, AssetMaturityService assetMaturityService) {
        super(notificationTemplateService, dingtalkService, edsInstanceHelper, edsConfigService);
        this.assetMaturityService = assetMaturityService;
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                CUSTOM_ASSET_EXPIRATION_INSPECTION_NOTIFICATION);
        Date expiry = ExpiredUtils.generateExpirationTime(ASSET_MATURITY_EXPIRY_DAYS, TimeUnit.DAYS);
        List<AssetMaturity> assetMaturityList = assetMaturityService.queryByLessThanExpiry(expiry);
        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put(ASSET_MATURITIES_FIELD, assetMaturityList)
                .put(EXPIRY_DAYS_FIELD, ASSET_MATURITY_EXPIRY_DAYS)
                .build());
    }

}
