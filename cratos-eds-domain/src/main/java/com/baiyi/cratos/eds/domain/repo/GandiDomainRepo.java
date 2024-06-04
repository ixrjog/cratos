package com.baiyi.cratos.eds.domain.repo;

import com.baiyi.cratos.eds.core.config.EdsGandiConfigModel;
import com.baiyi.cratos.eds.domain.model.GandiDomain;
import com.baiyi.cratos.eds.domain.service.GandiDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午2:48
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class GandiDomainRepo {

    private final GandiDomainService gandiDomainService;

    public List<GandiDomain.Domain> queryDomains(EdsGandiConfigModel.Gandi gandi) {
        String authorization = "";
        if (gandi.getCred()
                .hasToken()) {
            authorization = gandi.getCred()
                    .toBearer();
        } else if (gandi.getCred()
                .hasApikey()) {
            authorization = gandi.getCred()
                    .toApikey();
        }
        return gandiDomainService.queryDomains(authorization);
    }

}
