package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.credential.ApplicationCredentialParam;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.credential.ApplicationCredentialVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.facade.ApplicationCredentialFacade;
import com.baiyi.cratos.facade.EdsFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/15 10:34
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationCredentialFacadeImpl implements ApplicationCredentialFacade {

    private final EdsFacade edsFacade;
    private final EdsAssetIndexFacade indexFacade;

    @Override
    public DataTable<ApplicationCredentialVO.Credential> queryApplicationCredentialPage(
            ApplicationCredentialParam.ApplicationCredentialPageQuery pageQuery) {
        EdsInstanceParam.AssetPageQuery assetPageQuery = pageQuery.toAssetPageQuery();
        assetPageQuery.setAssetType(EdsAssetTypeEnum.ALIYUN_KMS_SECRET.name());
        //assetPageQuery.setQueryByTag(buildQueryByTag());
        DataTable<EdsAssetVO.Asset> table = edsFacade.queryEdsInstanceAssetPage(assetPageQuery);
        return new DataTable<>(table.getData()
                .stream()
                .map(this::toApplicationCredential)
                .toList(), table.getTotalNum());
    }

    private ApplicationCredentialVO.Credential toApplicationCredential(EdsAssetVO.Asset asset) {
        return ApplicationCredentialVO.Credential.builder()
                .asset(asset)
                .build();
    }

}
