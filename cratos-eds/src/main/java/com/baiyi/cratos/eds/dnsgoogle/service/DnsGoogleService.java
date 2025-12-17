package com.baiyi.cratos.eds.dnsgoogle.service;

import com.baiyi.cratos.eds.dnsgoogle.model.DnsGoogleModel;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/16 13:53
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface DnsGoogleService {

    @GetExchange("/resolve")
    DnsGoogleModel.DnsResolve resolve(@RequestParam Map<String, String> param);

}
