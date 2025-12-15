package com.baiyi.cratos.eds.domain.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.domain.model.GodaddyDomain;
import com.baiyi.cratos.eds.domain.service.GodaddyDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午2:47
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class GodaddyDomainRepo {

    private final GodaddyDomainService godaddyDomainService;

    public GodaddyDomain.Domain getDomain(EdsConfigs.Godaddy godaddy, String domain) {
        String authorization = godaddy.getCred()
                .toSsoKey();
        return godaddyDomainService.getDomain(authorization, godaddy.getCustomerId(), domain);
    }

    public List<GodaddyDomain.Domain> queryDomains(EdsConfigs.Godaddy godaddy) {
        String authorization = godaddy.getCred()
                .toSsoKey();
        return godaddyDomainService.queryDomains(authorization, godaddy.getCustomerId());
    }

}
