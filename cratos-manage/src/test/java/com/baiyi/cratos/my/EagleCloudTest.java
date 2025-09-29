package com.baiyi.cratos.my;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.event.EagleCloudEventParam;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 16:11
 * &#064;Version 1.0
 */
public class EagleCloudTest extends BaseUnit {

    @Test
    void test() {
        EagleCloudEventParam.SaseHook saseHook = EagleCloudEventParam.SaseHook.builder()
                .actionUrl("https://sase.xxxx.com:5443/dlp/dlp-log/event?event_id=event_5zz2J5z8D63")
                .content(
                        "敏感数据敏感文件外发告警通知  \n事件名称：使用外设发送S3中等敏感文件  \n事件ID：event_5zz2J5z8D63  \n告警内容：使用Chrome，外发1个敏感文件: 20250929080801792017.png  \n告警对象：Faronbi Ibrahim Oladipo  \n告警时间：2025-09-29 15:09")
                .dataTime("20250929150958")
                .timestamp(new java.util.Date(1759129798592L))
                .title("数据安全告警通知")
                .build();

        EagleCloudEventParam.Content content = EagleCloudEventParam.Content.parse(saseHook);
        System.out.println(content.toString());
    }


}
