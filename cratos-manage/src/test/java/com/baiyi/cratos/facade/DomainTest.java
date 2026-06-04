package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.DomainService;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/25 10:16
 * &#064;Version 1.0
 */
public class DomainTest extends BaseUnit {

    @Resource
    private DomainService domainService;

    @Resource
    private EdsAssetService edsAssetService;

    @Test
    void test() {
        List<Domain> domainList = domainService.selectAll();
        for (Domain domain : domainList) {
            if (domain.getDomainType()
                    .equals(EdsAssetTypeEnum.AWS_DOMAIN.name())) {
                List<EdsAsset> assets = edsAssetService.queryInstanceAssetByTypeAndKey(
                        94, EdsAssetTypeEnum.AWS_DOMAIN.name(), domain.getName());
                if (assets.size() == 1) {
                    domain.setAssetId(assets.getFirst()
                                              .getId());
                    domain.setInstanceId(assets.getFirst()
                                                 .getInstanceId());
                    domainService.updateByPrimaryKey(domain);
                }
            }
        }
    }

}
