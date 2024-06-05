package com.baiyi.cratos.eds.domain.service;

import com.baiyi.cratos.eds.domain.model.GodaddyDomain;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/5 下午3:06
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface GodaddyDomainService {

    /**
     * https://developer.godaddy.com/doc/endpoint/domains#/
     *
     * @param authorization Example: Authorization: sso-key [API_KEY]:[API_SECRET]
     * @return
     */
    @GetExchange("v2/customers/{customerId}/domains/{domain}")
    GodaddyDomain.Domain getDomain(@RequestHeader("Authorization") String authorization,
                             @PathVariable String customerId, @PathVariable String domain);

    @GetExchange("v2/customers/{customerId}/domains/")
    List<GodaddyDomain.Domain> queryDomains(@RequestHeader("Authorization") String authorization,
                                         @PathVariable String customerId);

}