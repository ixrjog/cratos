package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.facade.inspection.base.BaseInspection;
import com.baiyi.cratos.service.DomainService;
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
 * &#064;Date  2024/5/8 下午4:29
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class DomainInspection extends BaseInspection {

    private final DomainService domainService;

    private static final int DOMAIN_EXPIRY_DAYS = 30;
    public static final String DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION = "DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION";
    private static final String DOMAINS_FIELD = "domains";
    private static final String EXPIRY_DAYS_FIELD = "expiryDays";

    public DomainInspection(NotificationTemplateService notificationTemplateService,
                            DingtalkService dingtalkService, EdsInstanceHelper edsInstanceHelper,
                            EdsConfigService edsConfigService, DomainService domainService) {
        super(notificationTemplateService, dingtalkService, edsInstanceHelper, edsConfigService);
        this.domainService = domainService;
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION);
        Date expiry = ExpiredUtils.generateExpirationTime(DOMAIN_EXPIRY_DAYS, TimeUnit.DAYS);
        List<Domain> domainList = domainService.queryByLessThanExpiry(expiry);
        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put(DOMAINS_FIELD, domainList)
                .put(EXPIRY_DAYS_FIELD, DOMAIN_EXPIRY_DAYS)
                .build());
    }

}
