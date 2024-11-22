package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.param.http.template.NotificationTemplateParam;
import com.baiyi.cratos.mapper.NotificationTemplateMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 下午5:43
 * &#064;Version 1.0
 */
public interface NotificationTemplateService extends BaseUniqueKeyService<NotificationTemplate, NotificationTemplateMapper> {

    DataTable<NotificationTemplate> queryNotificationTemplatePage(
            NotificationTemplateParam.NotificationTemplatePageQuery pageQuery);

}
