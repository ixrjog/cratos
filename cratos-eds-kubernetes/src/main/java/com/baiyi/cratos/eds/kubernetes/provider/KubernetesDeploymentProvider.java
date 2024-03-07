package com.baiyi.cratos.eds.kubernetes.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/7 10:26
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT)
public class KubernetesDeploymentProvider<T> extends BaseEdsInstanceProvider<EdsKubernetesConfigModel.Kubernetes, T> {

    @Override
    protected List<T> listEntities(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return null;
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, T entity) {
        return null;
    }

}
