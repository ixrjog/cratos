package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.param.http.template.NotificationTemplateParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface NotificationTemplateMapper extends Mapper<NotificationTemplate> {

    List<NotificationTemplate> queryPageByParam(NotificationTemplateParam.NotificationTemplatePageQuery pageQuery);

}