package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.param.http.template.NotificationTemplateParam;
import com.baiyi.cratos.domain.view.template.NotificationTemplateVO;
import com.baiyi.cratos.facade.NotificationTemplateFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.wrapper.NotificationTemplateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午9:56
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationTemplateFacadeImpl implements NotificationTemplateFacade {

    private final NotificationTemplateService notificationTemplateService;
    private final NotificationTemplateWrapper notificationTemplateWrapper;

    @Override
    public DataTable<NotificationTemplateVO.NotificationTemplate> queryNotificationTemplatePage(
            NotificationTemplateParam.NotificationTemplatePageQuery pageQuery) {
        DataTable<NotificationTemplate> table = notificationTemplateService.queryNotificationTemplatePage(pageQuery);
        return notificationTemplateWrapper.wrapToTarget(table);
    }

    @Override
    public void addNotificationTemplate(NotificationTemplateParam.AddNotificationTemplate addNotificationTemplate) {
        NotificationTemplate notificationTemplate = addNotificationTemplate.toTarget();
        if (notificationTemplateService.getByUniqueKey(notificationTemplate) == null) {
            notificationTemplateService.add(notificationTemplate);
        }
    }

    @Override
    public void updateNotificationTemplate(
            NotificationTemplateParam.UpdateNotificationTemplate updateNotificationTemplate) {
        NotificationTemplate notificationTemplate = updateNotificationTemplate.toTarget();
        notificationTemplate.setNotificationTemplateKey(null);
        notificationTemplateService.updateByPrimaryKeySelective(notificationTemplate);
    }

}
