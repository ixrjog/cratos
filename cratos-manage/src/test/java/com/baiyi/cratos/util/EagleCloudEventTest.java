package com.baiyi.cratos.util;

import com.baiyi.cratos.domain.param.http.event.EagleCloudEventParam;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/17 11:05
 * &#064;Version 1.0
 */
public class EagleCloudEventTest {


    public static final String HOOK = """
            敏感数据敏感文件外发告警通知  \n事件名称：发送的敏感文件大小大于历史30天均值10倍  \n事件ID：e7589fa7-c10e-42a8-a25f-b362dbfba94c\
              \\  \n告警内容：使用Chrome，外发1个敏感文件: 孟加拉、尼日利亚宣传视频.rar  \n告警对象：王楠 Axel  \n告警时间：2025-10-17\
              \\ 03:26
            """;

    @Test
    void test() {
        EagleCloudEventParam.SaseHook saseHook = EagleCloudEventParam.SaseHook.builder()
                .content(HOOK)
                .build();
        EagleCloudEventParam.Content content = EagleCloudEventParam.Content.parse(saseHook);
        System.out.println(content);
    }

}
