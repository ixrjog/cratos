package com.baiyi.cratos.eds.domain.service;

import com.baiyi.cratos.eds.domain.model.GandiDomain;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午2:51
 * &#064;Version 1.0
 */
//@HttpExchange(accept = "application/json", contentType = "application/json;charset=utf-8")
public interface GandiDomainService {

    /**
     * https://api.gandi.net/docs/domains/
     * @param authorization Example: Apikey your-api-key
     * @return
     */
    @GetExchange("/v5/domain/domains")
    List<GandiDomain.Domain> queryDomains(@RequestHeader("Authorization") String authorization);

}