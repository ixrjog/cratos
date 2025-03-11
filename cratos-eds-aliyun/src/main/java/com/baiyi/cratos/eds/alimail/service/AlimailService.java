package com.baiyi.cratos.eds.alimail.service;

import com.baiyi.cratos.eds.alimail.model.AlimailTokenResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 14:22
 * &#064;Version 1.0
 */
@HttpExchange(contentType = "application/x-www-form-urlencoded")
public interface AlimailService {

    @GetExchange("/oauth2/v2.0/token")
    AlimailTokenResult.Token getToken(@RequestHeader("Authorization") String bearerToken,
                                           @RequestParam Map<String, String> param);


}
