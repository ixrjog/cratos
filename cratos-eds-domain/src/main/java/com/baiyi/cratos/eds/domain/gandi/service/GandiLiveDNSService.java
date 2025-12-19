package com.baiyi.cratos.eds.domain.gandi.service;

import com.baiyi.cratos.eds.domain.gandi.GandiService;
import com.baiyi.cratos.eds.domain.gandi.model.GandiLiveDNS;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/19 16:21
 * &#064;Version 1.0
 */
public interface GandiLiveDNSService extends GandiService {

    @GetExchange("/v5/livedns/domains/{fqdn}/records")
    List<GandiLiveDNS.Record> queryRecords(@PathVariable String fqdn);

}
