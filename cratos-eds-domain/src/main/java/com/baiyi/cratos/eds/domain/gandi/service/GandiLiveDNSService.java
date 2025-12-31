package com.baiyi.cratos.eds.domain.gandi.service;

import com.baiyi.cratos.eds.domain.gandi.GandiService;
import com.baiyi.cratos.eds.domain.gandi.model.GandiLiveDNS;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PatchExchange;

import java.util.List;

/**
 * https://api.gandi.net/docs/livedns/
 * &#064;Author  baiyi
 * &#064;Date  2025/12/19 16:21
 * &#064;Version 1.0
 */
public interface GandiLiveDNSService extends GandiService {

    @GetExchange("/v5/livedns/domains/{fqdn}/records")
    List<GandiLiveDNS.Record> queryRecords(@PathVariable String fqdn) throws WebClientResponseException;

    @GetExchange("/v5/livedns/domains/{fqdn}/records/{rrsetName}/{rrsetType}")
    GandiLiveDNS.Record getRecordByNameAndType(@PathVariable String fqdn, @PathVariable String rrsetName,
                                               @PathVariable String rrsetType) throws WebClientResponseException;

    @PatchExchange("/v5/livedns/domains/{fqdn}/records/{rrsetName}/{rrsetType}")
    GandiLiveDNS.Record updateRecordByNameAndType(@PathVariable String fqdn, @PathVariable String rrsetName,
                                                  @PathVariable String rrsetType) throws WebClientResponseException;

    @PatchExchange("/v5/livedns/domains/{fqdn}/records/{rrsetName}/{rrsetType}")
    GandiLiveDNS.Record createRecordByNameAndType(@PathVariable String fqdn, @PathVariable String rrsetName,
                                                  @PathVariable String rrsetType) throws WebClientResponseException;

}
