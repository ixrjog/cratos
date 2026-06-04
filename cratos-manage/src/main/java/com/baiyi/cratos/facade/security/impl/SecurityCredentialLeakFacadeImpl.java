package com.baiyi.cratos.facade.security.impl;

import com.baiyi.cratos.common.util.RequestSignUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.view.security.LeakedCredentialVO;
import com.baiyi.cratos.facade.security.SecurityCredentialLeakFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCESS_KEY_IDS;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.HASH_SHA256;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/21 14:47
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SecurityCredentialLeakFacadeImpl implements SecurityCredentialLeakFacade {

    private final EdsInstanceService instanceService;
    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService assetIndexService;

    @Override
    public LeakedCredentialVO.Credentials detectLeak(String queryCredential) {
        final String credential = queryCredential.trim();
        // GCP API Key 检查
        String credentialHashSha256 = RequestSignUtil.sha256Hex(credential);
        List<EdsAssetIndex> apiKeyIndices = assetIndexService.queryIndexByNameAndValue(
                HASH_SHA256, credentialHashSha256);
        if (!CollectionUtils.isEmpty(apiKeyIndices)) {
            // 有泄漏
            EdsAsset asset = edsAssetService.getById(apiKeyIndices.getFirst()
                                                             .getAssetId());
            if (asset != null) {
                String instanceName = Optional.ofNullable(instanceService.getById(asset.getInstanceId()))
                        .map(EdsInstance::getInstanceName)
                        .orElse("--");
                LeakedCredentialVO.GcpApiKey gcpApiKey = LeakedCredentialVO.GcpApiKey.builder()
                        .instanceName(instanceName)
                        .asset(asset)
                        .build();
                return LeakedCredentialVO.Credentials.of(gcpApiKey);
            }
        }
        // AccessKey
        List<EdsAssetIndex> akIndices = assetIndexService.queryIndexByNameAndValueLike(
                CLOUD_ACCESS_KEY_IDS, credential);
        if (!CollectionUtils.isEmpty(akIndices)) {
            List<LeakedCredentialVO.AccessKey> accessKeys = akIndices.stream()
                    .map(index -> {
                        EdsAsset asset = edsAssetService.getById(index.getAssetId());
                        if (asset == null) {
                            return null;
                        }
                        String instanceName = Optional.ofNullable(instanceService.getById(asset.getInstanceId()))
                                .map(EdsInstance::getInstanceName)
                                .orElse("--");
                        Map<String, EdsAssetIndex> indexMap = assetIndexService.queryIndexByAssetId(asset.getId())
                                .stream()
                                .collect(Collectors.toMap(EdsAssetIndex::getName, e -> e, (a, b) -> a));
                        return LeakedCredentialVO.AccessKey.builder()
                                .instanceName(instanceName)
                                .asset(asset)
                                .indexMap(indexMap)
                                .build();
                    })
                    .filter(Objects::nonNull)
                    .toList();
            return LeakedCredentialVO.Credentials.of(accessKeys);
        }
        return LeakedCredentialVO.Credentials.SAFE;
    }

}
