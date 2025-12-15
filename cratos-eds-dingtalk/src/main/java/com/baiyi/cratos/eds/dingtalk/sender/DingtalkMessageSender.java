package com.baiyi.cratos.eds.dingtalk.sender;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkMessageModel;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkTokenModel;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkMessageParam;
import com.baiyi.cratos.eds.dingtalk.repo.base.BaseDingtalkToken;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/24 17:07
 * &#064;Version 1.0
 */
@Component
public class DingtalkMessageSender extends BaseDingtalkToken {

    public DingtalkMessageSender(RedisUtil redisUtil, DingtalkService dingtalkService) {
        super(redisUtil, dingtalkService);
    }

    public DingtalkMessageModel.AsyncSendResult asyncSend(EdsConfigs.Dingtalk dingtalk,
                                                          DingtalkMessageParam.AsyncSendMessage asyncSendMessage) {
        DingtalkTokenModel.TokenResult tokenResult = getToken(dingtalk);
        asyncSendMessage.setAgentId(Long.valueOf(dingtalk.getApp()
                .getAgentId()));
        return dingtalkService.asyncSend(tokenResult.getAccessToken(), asyncSendMessage);
    }

}
