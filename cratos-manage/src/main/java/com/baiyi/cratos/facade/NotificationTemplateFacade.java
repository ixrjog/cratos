package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.template.NotificationTemplateParam;
import com.baiyi.cratos.domain.view.template.NotificationTemplateVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午9:56
 * &#064;Version 1.0
 */
public interface NotificationTemplateFacade {

    DataTable<NotificationTemplateVO.NotificationTemplate> queryNotificationTemplatePage(
            NotificationTemplateParam.NotificationTemplatePageQuery pageQuery);

    void updateNotificationTemplate(NotificationTemplateParam.UpdateNotificationTemplate updateNotificationTemplate);

}
