package com.baiyi.cratos.eds.domain.gandi.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.domain.gandi.GandiServiceFactory;
import com.baiyi.cratos.eds.domain.gandi.model.GandiDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午2:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GandiDomainRepo {

    public static List<GandiDomain.Domain> queryDomains(EdsConfigs.Gandi config) {
        return GandiServiceFactory.createDomainService(config)
                .queryDomains();
    }

}
