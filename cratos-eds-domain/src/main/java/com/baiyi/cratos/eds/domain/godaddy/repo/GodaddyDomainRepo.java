package com.baiyi.cratos.eds.domain.godaddy.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.domain.godaddy.GodaddyServiceFactory;
import com.baiyi.cratos.eds.domain.godaddy.model.GodaddyDomain;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午2:47
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GodaddyDomainRepo {

    public static GodaddyDomain.Domain getDomain(EdsConfigs.Godaddy config, String domain) {
        return GodaddyServiceFactory.createDomainService(config)
                .getDomain(config.getCustomerId(), domain);
    }

    public static List<GodaddyDomain.Domain> queryDomains(EdsConfigs.Godaddy config) {
        return GodaddyServiceFactory.createDomainService(config)
                .queryDomains(config.getCustomerId());
    }

}
