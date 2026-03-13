package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.util.SreBridgeUtils;
import com.baiyi.cratos.eds.core.util.SreEventFormatter;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.facade.inspection.base.BaseInspectionTask;
import com.baiyi.cratos.service.CertificateService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.CERTIFICATE_EXPIRATION_INSPECTION_NOTIFICATION;
import static com.baiyi.cratos.eds.core.util.SreEventFormatter.EVENT_ID;
import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 下午4:30
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class CertificateInspectionTask extends BaseInspectionTask {

    private final CertificateService certificateService;

    private static final int CERTIFICATE_EXPIRY_DAYS = 30;
    private static final String CERTIFICATES_FIELD = "certificates";
    private static final String EXPIRY_DAYS_FIELD = "expiryDays";

    public CertificateInspectionTask(NotificationTemplateService notificationTemplateService,
                                     DingtalkService dingtalkService, EdsInstanceQueryHelper edsInstanceQueryHelper,
                                     EdsConfigService edsConfigService, CertificateService certificateService) {
        super(notificationTemplateService, dingtalkService, edsInstanceQueryHelper, edsConfigService);
        this.certificateService = certificateService;
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                CERTIFICATE_EXPIRATION_INSPECTION_NOTIFICATION);
        Date expiry = ExpiredUtils.generateExpirationTime(CERTIFICATE_EXPIRY_DAYS, TimeUnit.DAYS);
        List<Certificate> certificateList = certificateService.queryByLessThanExpiry(expiry);
        certificateList.forEach(this::publish);

        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put(CERTIFICATES_FIELD, certificateList)
                .put(EXPIRY_DAYS_FIELD, CERTIFICATE_EXPIRY_DAYS)
                .build());
    }

    private void publish(Certificate certificate) {
        try {
            Map<String, String> ext = Map.of(EVENT_ID, PasswordGenerator.generateNo());
            Map<String, String> targetContent = Map.ofEntries(
                    entry("certificateId", certificate.getCertificateId()),
                    entry("domain", certificate.getName()),
                    entry("domains", certificate.getDomainName()),
                    entry("certificateType", certificate.getCertificateType()),
                    entry("notBefore", TimeUtils.parse(certificate.getNotBefore(), Global.ISO8601)),
                    entry("notAfter", TimeUtils.parse(certificate.getNotAfter(), Global.ISO8601))
            );
            com.baiyi.cratos.domain.model.SreBridgeModel.Event event = com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                    .operator(OPERATOR)
                    .action(SreEventFormatter.Action.INSPECT_CERTIFICATE.getValue())
                    .description(StringFormatter.arrayFormat(
                            "Domain certificate inspection task found that certificate {} will expire within {} days", certificate.getName(),
                            CERTIFICATE_EXPIRY_DAYS
                    ))
                    .target(certificate.getName())
                    .targetContent(SreEventFormatter.mapToJson(targetContent))
                    .affection("")
                    .severity("low")
                    .status("executed")
                    .ext(ext)
                    .build();
            SreBridgeUtils.publish(event);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
