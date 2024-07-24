package com.baiyi.cratos.facade.inspection;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.BeetlUtil;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.EdsInstanceHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkRobotService;
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

    private static final int DOMAIN_EXPIRY_DAYS = 60;

    public static final String DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION = "DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION";

    private final DomainService domainService;

    private static final String DOMAINS_FIELD = "domains";

    private static final String EXPIRY_DAYS_FIELD = "expiryDays";

    public DomainInspection(NotificationTemplateService notificationTemplateService,
                            DingtalkRobotService dingtalkRobotService, EdsInstanceHelper edsInstanceHelper,
                            EdsConfigService edsConfigService, DomainService domainService) {
        super(notificationTemplateService, dingtalkRobotService, edsInstanceHelper, edsConfigService);
        this.domainService = domainService;
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION);
        Date expiry = ExpiredUtil.generateExpirationTime(DOMAIN_EXPIRY_DAYS, TimeUnit.DAYS);
        List<Domain> domainList = domainService.queryByLessThanExpiry(expiry);
        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put(DOMAINS_FIELD, domainList)
                .put(EXPIRY_DAYS_FIELD, DOMAIN_EXPIRY_DAYS)
                .build());
    }

}
