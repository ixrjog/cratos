package com.baiyi.cratos.event;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.message.TestMessage;
import com.baiyi.cratos.event.publisher.DemoEventPublisher;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 上午11:39
 * &#064;Version 1.0
 */
public class EventTest extends BaseUnit {

    @Resource
    private DemoEventPublisher demoEventPublisher;

    @Test
    void publishTest() {
        TestMessage.Test test = TestMessage.Test.builder()
                .msg("Hello World")
                .build();
        demoEventPublisher.publish(test);
//        try {
//            Thread.sleep(1000 * 30);
//        } catch (InterruptedException ignored) {
//        }
    }

}
