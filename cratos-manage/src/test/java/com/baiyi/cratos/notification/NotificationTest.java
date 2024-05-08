package com.baiyi.cratos.notification;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.BeetlUtil;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.facade.InspectionNotificationFacade;
import com.baiyi.cratos.service.DomainService;
import com.baiyi.cratos.service.NotificationTemplateService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 下午5:47
 * &#064;Version 1.0
 */
public class NotificationTest extends BaseUnit {

    @Resource
    private NotificationTemplateService notificationTemplateService;

    @Resource
    private DomainService domainService;

    @Resource
    private InspectionNotificationFacade inspectionNotificationFacade;

    @Test
    void test() throws IOException {
        NotificationTemplate notificationTemplate = notificationTemplateService.getById(1);
        Date expiry = ExpiredUtil.generateExpirationTime(130, TimeUnit.DAYS);
        List<Domain> domainList = domainService.queryByLessThanExpiry(expiry);
        String msg = BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put("domains", domainList)
                .build());

        System.out.println(msg);
    }

    @Test
    void taskTest() {
        inspectionNotificationFacade.domainInspectionTask();
        inspectionNotificationFacade.certificateInspectionTask();
    }

}
