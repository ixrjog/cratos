package com.baiyi.cratos.eds.domain.godaddy.service;

import com.baiyi.cratos.eds.domain.godaddy.GodaddyService;
import com.baiyi.cratos.eds.domain.godaddy.model.GodaddyDomain;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * https://developer.godaddy.com/doc/endpoint/domains#/
 * &#064;Author  baiyi
 * &#064;Date  2024/6/5 下午3:06
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface GodaddyDomainService extends GodaddyService {

    @GetExchange("v2/customers/{customerId}/domains/{domain}")
    GodaddyDomain.Domain getDomain(@PathVariable String customerId, @PathVariable String domain);

    @GetExchange("v2/customers/{customerId}/domains/")
    List<GodaddyDomain.Domain> queryDomains(@PathVariable String customerId);

}