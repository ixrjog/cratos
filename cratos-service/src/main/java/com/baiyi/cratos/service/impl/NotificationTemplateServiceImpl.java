package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.param.http.template.NotificationTemplateParam;
import com.baiyi.cratos.mapper.NotificationTemplateMapper;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 下午5:43
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private final NotificationTemplateMapper notificationTemplateMapper;

    @Override
    public NotificationTemplate getByUniqueKey(@NonNull NotificationTemplate record) {
        Example example = new Example(NotificationTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("notificationTemplateKey", record.getNotificationTemplateKey())
                .andEqualTo("lang", record.getLang());
        return notificationTemplateMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<NotificationTemplate> queryNotificationTemplatePage(
            NotificationTemplateParam.NotificationTemplatePageQuery pageQuery) {
        Page<NotificationTemplate> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<NotificationTemplate> data = notificationTemplateMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:NOTIFICATIONTEMPLATE:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
