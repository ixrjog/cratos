package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.param.http.template.NotificationTemplateParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NotificationTemplateMapper extends Mapper<NotificationTemplate> {

    List<NotificationTemplate> queryPageByParam(NotificationTemplateParam.NotificationTemplatePageQuery pageQuery);

}