package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.util.SreBridgeUtils;
import com.baiyi.cratos.eds.core.util.SreEventFormatter;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.facade.inspection.base.BaseInspectionTask;
import com.baiyi.cratos.service.DomainService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION;
import static com.baiyi.cratos.eds.core.util.SreEventFormatter.EVENT_ID;
import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 下午4:29
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class DomainInspectionTask extends BaseInspectionTask {

    private final DomainService domainService;

    private static final int DOMAIN_EXPIRY_DAYS = 30;
    private static final String DOMAINS_FIELD = "domains";
    private static final String EXPIRY_DAYS_FIELD = "expiryDays";

    public DomainInspectionTask(NotificationTemplateService notificationTemplateService,
                                DingtalkService dingtalkService, EdsInstanceQueryHelper edsInstanceQueryHelper,
                                EdsConfigService edsConfigService, DomainService domainService) {
        super(notificationTemplateService, dingtalkService, edsInstanceQueryHelper, edsConfigService);
        this.domainService = domainService;
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION);
        Date expiry = ExpiredUtils.generateExpirationTime(DOMAIN_EXPIRY_DAYS, TimeUnit.DAYS);
        List<Domain> domainList = domainService.queryByLessThanExpiry(expiry);
        // SRE
        domainList.forEach(this::publish);
        return BeetlUtil.renderTemplate(
                notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                        .put(DOMAINS_FIELD, domainList)
                        .put(EXPIRY_DAYS_FIELD, DOMAIN_EXPIRY_DAYS)
                        .build()
        );
    }

    private void publish(Domain domain) {
        try {
            Map<String, String> ext = Map.of(EVENT_ID, PasswordGenerator.generateNo());
            Map<String, String> targetContent = Map.ofEntries(
                    entry("remark", domain.getComment()), entry("domainType", domain.getDomainType()),
                    entry("domain", domain.getName()),
                    entry("registrationTime", TimeUtils.parse(domain.getRegistrationTime(), Global.ISO8601)),
                    entry("expiry", TimeUtils.parse(domain.getExpiry(), Global.ISO8601))
            );
            com.baiyi.cratos.domain.model.SreBridgeModel.Event event = com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                    .operator(OPERATOR)
                    .action(SreEventFormatter.Action.INSPECT_DOMAIN.getValue())
                    .description(StringFormatter.arrayFormat(
                            "Domain inspection task found that {} will expire within {} days", domain.getName(),
                            DOMAIN_EXPIRY_DAYS
                    ))
                    .target(domain.getName())
                    .targetContent(SreEventFormatter.mapToJson(targetContent))
                    .type(SreEventFormatter.Type.INSPECTION.getValue())
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
