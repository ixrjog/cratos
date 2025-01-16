package com.baiyi.cratos.domain.util.googledns.service;

import com.baiyi.cratos.domain.util.googledns.model.DnsGoogleModel;
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
