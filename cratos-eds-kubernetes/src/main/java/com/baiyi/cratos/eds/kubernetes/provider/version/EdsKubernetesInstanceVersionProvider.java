package com.baiyi.cratos.eds.kubernetes.provider.version;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.version.BaseEdsInstanceVersionProvider;
import com.baiyi.cratos.eds.kubernetes.repo.version.KubernetesVersionRepo;
import io.fabric8.kubernetes.client.VersionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 14:48
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES)
public class EdsKubernetesInstanceVersionProvider implements BaseEdsInstanceVersionProvider<EdsConfigs.Kubernetes> {

    private final KubernetesVersionRepo kubernetesVersionRepo;

    @Override
    public String getVersion(ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance) {
        VersionInfo versionInfo = kubernetesVersionRepo.getVersion(instance.getConfig());
        return versionInfo.getGitVersion();
    }

}
