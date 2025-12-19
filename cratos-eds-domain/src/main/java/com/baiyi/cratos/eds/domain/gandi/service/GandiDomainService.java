package com.baiyi.cratos.eds.domain.gandi.service;

import com.baiyi.cratos.eds.domain.gandi.GandiService;
import com.baiyi.cratos.eds.domain.gandi.model.GandiDomain;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

/**
 * https://api.gandi.net/docs/domains/
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午2:51
 * &#064;Version 1.0
 */
//@HttpExchange(accept = "application/json", contentType = "application/json;charset=utf-8")
public interface GandiDomainService extends GandiService {

    @GetExchange("/v5/domain/domains")
    List<GandiDomain.Domain> queryDomains();

}