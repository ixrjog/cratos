package com.baiyi.cratos.event.publisher;

import com.baiyi.cratos.common.annotation.EventPublisher;
import com.baiyi.cratos.common.constants.TopicConstants;
import com.baiyi.cratos.common.event.IEventPublisher;
import com.baiyi.cratos.domain.message.TestMessage;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 下午1:32
 * &#064;Version 1.0
 */
@Component
public class DemoEventPublisher implements IEventPublisher<TestMessage.Test> {

    @Override
    @EventPublisher(topic = TopicConstants.TEST_TOPIC)
    public void publish(TestMessage.Test test) {
    }

}
