package com.baiyi.cratos.eds.dingtalk.service;

import com.baiyi.cratos.eds.dingtalk.model.DingtalkToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午11:20
 * @Version 1.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json;charset=utf-8")
public interface DingtalkTokenService {

    @GetExchange("/gettoken?appkey={appkey}&appsecret={appsecret}")
    DingtalkToken.TokenResult getToken(@PathVariable String appkey, @PathVariable String appsecret);

}
