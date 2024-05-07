package com.baiyi.cratos.common.event.topic;

import com.baiyi.cratos.domain.annotation.Topic;
import org.springframework.aop.support.AopUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 上午11:04
 * &#064;Version 1.0
 */
public interface ITopicAnnotate extends ITopic {

    // 从注解中获取
    default String getTopic() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(Topic.class)
                .name();
    }

}
