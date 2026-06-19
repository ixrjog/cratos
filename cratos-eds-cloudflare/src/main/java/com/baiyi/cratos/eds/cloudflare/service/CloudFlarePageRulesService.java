package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.CloudFlarePageRules;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudFlareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.base.CloudFlareService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/16 09:32
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface CloudFlarePageRulesService extends CloudFlareService {

    @GetExchange("/zones/{zoneId}/pagerules")
    CloudFlareHttpResult<List<CloudFlarePageRules.PageRule>> listPageRules(@PathVariable String zoneId);

}