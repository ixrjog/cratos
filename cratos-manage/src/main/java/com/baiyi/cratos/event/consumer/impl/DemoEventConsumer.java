package com.baiyi.cratos.event.consumer.impl;

import com.baiyi.cratos.common.constants.EventTopicConstants;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.Topic;
import com.baiyi.cratos.domain.message.TestMessage;
import com.baiyi.cratos.event.Event;
import com.baiyi.cratos.event.consumer.base.BaseEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 上午10:59
 * &#064;Version 1.0
 */
@Slf4j
@Topic(name = EventTopicConstants.TEST_TOPIC)
@Component
public class DemoEventConsumer extends BaseEventConsumer<TestMessage.Test> {

    @Override
    protected void handleMessage(Event<TestMessage.Test> noticeEvent) {
        final String msg = "DemoEventConsumer test msg: {}.";
        log.info(StringFormatter.arrayFormat(msg, noticeEvent.getMessage()
                .getMsg()));
    }

}
