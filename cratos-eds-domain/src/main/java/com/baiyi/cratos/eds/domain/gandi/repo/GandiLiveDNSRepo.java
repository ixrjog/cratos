package com.baiyi.cratos.eds.domain.gandi.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.domain.gandi.GandiServiceFactory;
import com.baiyi.cratos.eds.domain.gandi.model.GandiLiveDNS;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/19 16:27
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GandiLiveDNSRepo {

    /**
     *
     * @param config
     * @param domain not FQDN
     * @return
     */
    public static List<GandiLiveDNS.Record> queryRecords(EdsConfigs.Gandi config, String domain) {
        return GandiServiceFactory.createLiveDNSService(config)
                .queryRecords(domain);
    }

}