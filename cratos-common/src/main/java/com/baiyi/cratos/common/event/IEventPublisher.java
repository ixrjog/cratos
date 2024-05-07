package com.baiyi.cratos.common.event;

import com.baiyi.cratos.domain.message.IEventMessage;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 下午1:31
 * &#064;Version 1.0
 */
public interface IEventPublisher<T extends IEventMessage> {

    void publish(T t);

}
