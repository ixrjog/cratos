package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.facade.inspection.base.BaseInspection;
import com.baiyi.cratos.service.CertificateService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 下午4:30
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class CertificateInspection extends BaseInspection {

    private final CertificateService certificateService;

    private static final int CERTIFICATE_EXPIRY_DAYS = 30;
    private static final String CERTIFICATES_FIELD = "certificates";
    private static final String EXPIRY_DAYS_FIELD = "expiryDays";
    public static final String CERTIFICATE_EXPIRATION_INSPECTION_NOTIFICATION = "CERTIFICATE_EXPIRATION_INSPECTION_NOTIFICATION";

    public CertificateInspection(NotificationTemplateService notificationTemplateService,
                                 DingtalkService dingtalkService, EdsInstanceHelper edsInstanceHelper,
                                 EdsConfigService edsConfigService, CertificateService certificateService) {
        super(notificationTemplateService, dingtalkService, edsInstanceHelper, edsConfigService);
        this.certificateService = certificateService;
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                CERTIFICATE_EXPIRATION_INSPECTION_NOTIFICATION);
        Date expiry = ExpiredUtils.generateExpirationTime(CERTIFICATE_EXPIRY_DAYS, TimeUnit.DAYS);
        List<Certificate> certificateList = certificateService.queryByLessThanExpiry(expiry);
        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put(CERTIFICATES_FIELD, certificateList)
                .put(EXPIRY_DAYS_FIELD, CERTIFICATE_EXPIRY_DAYS)
                .build());
    }

}
