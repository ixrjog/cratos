package com.baiyi.cratos.eds.aliyun.provider.kms;

import com.aliyun.sdk.service.kms20160120.models.ListKmsInstancesResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aliyun.model.AliyunKms;
import com.baiyi.cratos.eds.aliyun.repo.AliyunKmsRepo;
import com.baiyi.cratos.eds.core.BaseHasEndpointsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_KMS_ENDPOINT;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/8 17:15
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_KMS_INSTANCE)
public class EdsAliyunKmsInstanceAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsInstance> {

    public EdsAliyunKmsInstanceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                             EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected Set<String> listEndpoints(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        return Sets.newHashSet(Optional.of(instance)
                .map(ExternalDataSourceInstance::getEdsConfigModel)
                .map(EdsAliyunConfigModel.Aliyun::getKms)
                .map(EdsAliyunConfigModel.KMS::getEndpoints)
                .orElse(List.of()));
    }

    @Override
    protected List<AliyunKms.KmsInstance> listEntities(String endpoint,
                                                       ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        List<ListKmsInstancesResponseBody.KmsInstance> instances = AliyunKmsRepo.listInstances(endpoint,
                instance.getEdsConfigModel());
        if (CollectionUtils.isEmpty(instances)) {
            return List.of();
        }
        return toKmsInstance(endpoint, instance.getEdsConfigModel(), instances);
    }

    private List<AliyunKms.KmsInstance> toKmsInstance(String endpoint, EdsAliyunConfigModel.Aliyun configModel,
                                                      List<ListKmsInstancesResponseBody.KmsInstance> instances) {
        return instances.stream()
                .map(e -> AliyunKms.KmsInstance.builder()
                        .endpoint(endpoint)
                        .instance(AliyunKms.Instance.builder()
                                .kmsInstanceArn(e.getKmsInstanceArn())
                                .kmsInstanceId(e.getKmsInstanceId())
                                .build())
                        .build())
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunKms.KmsInstance entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getInstance()
                        .getKmsInstanceId())
                .nameOf(entity.getInstance()
                        .getKmsInstanceArn())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                                      EdsAsset edsAsset, AliyunKms.KmsInstance entity) {
        return List.of(toEdsAssetIndex(edsAsset, ALIYUN_KMS_ENDPOINT, entity.getEndpoint()));
    }

}