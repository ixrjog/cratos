package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsRequest;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsResponse;
import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.eds.aliyun.client.AliyunDyvmsClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/22 16:50
 * &#064;Version 1.0
 */
@Slf4j
public class AliyunDyvmsRepo {

    public static void callChannelFault(EdsConfigs.Aliyun aliyun, String channelName,
                                        String calledNumber) throws Exception {
        com.aliyun.dyvmsapi20170525.Client client = AliyunDyvmsClient.createClient(aliyun);
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        request.setCalledShowNumber("057123678062");
        request.setCalledNumber(calledNumber);
        request.setTtsCode("TTS_328540318");
        request.setTtsParam(JSONUtils.writeValueAsPrettyString(Map.of("channelName", channelName)));
        SingleCallByTtsResponse response = client.singleCallByTts(request);
        log.info(
                "渠道故障语音通知: channelName: {} callId {}", channelName, response.getBody()
                        .getCallId()
        );
    }

}
