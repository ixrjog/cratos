package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.view.template.NotificationTemplateVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午10:09
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationTemplateWrapper extends BaseDataTableConverter<NotificationTemplateVO.NotificationTemplate, NotificationTemplate> implements BaseWrapper<NotificationTemplateVO.NotificationTemplate> {

    @Override
    public void wrap(NotificationTemplateVO.NotificationTemplate vo) {
    }

}