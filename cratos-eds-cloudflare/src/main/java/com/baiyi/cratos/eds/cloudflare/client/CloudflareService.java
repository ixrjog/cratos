package com.baiyi.cratos.eds.cloudflare.client;

import com.baiyi.cratos.eds.cloudflare.model.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.model.VerifyUserTokens;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @Author baiyi
 * @Date 2024/3/1 16:40
 * @Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface CloudflareService {

    /**
     *
     * @param bearerToken -H "Authorization: Bearer API_TOKEN"
     * @return
     */
    @GetExchange("/v4/user/tokens/verify")
    CloudflareHttpResult<VerifyUserTokens.Result> verifyUserTokens(@RequestHeader("Authorization") String bearerToken);

}
