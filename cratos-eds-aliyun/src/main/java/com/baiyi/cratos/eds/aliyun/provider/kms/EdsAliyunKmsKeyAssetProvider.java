package com.baiyi.cratos.eds.aliyun.provider.kms;

import com.aliyun.sdk.service.kms20160120.models.ListKeysResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_KMS_ENDPOINT;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_KMS_INSTANCE_ID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/8 18:40
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_KMS_KEY)
public class EdsAliyunKmsKeyAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsKey> {

    public EdsAliyunKmsKeyAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
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
    protected List<AliyunKms.KmsKey> listEntities(String endpoint,
                                                  ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        List<ListKeysResponseBody.Key> keys = AliyunKmsRepo.listKeys(endpoint, instance.getEdsConfigModel());
        if (CollectionUtils.isEmpty(keys)) {
            return List.of();
        }
        return toKmsKey(endpoint, instance.getEdsConfigModel(), keys);
    }

    private List<AliyunKms.KmsKey> toKmsKey(String endpoint, EdsAliyunConfigModel.Aliyun configModel,
                                            List<ListKeysResponseBody.Key> keys) {
        return keys.stream()
                .map(e -> AliyunKmsRepo.describeKey(endpoint, configModel, e.getKeyId())
                        .map(keyMetadata -> {
                            AliyunKms.KeyMetadata metadata = BeanCopierUtils.copyProperties(keyMetadata,
                                    AliyunKms.KeyMetadata.class);
                            return AliyunKms.KmsKey.builder()
                                    .endpoint(endpoint)
                                    .metadata(metadata)
                                    .build();
                        })
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunKms.KmsKey entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getMetadata()
                        .getKeyId())
                .nameOf(entity.getMetadata()
                        .getArn())
                .assetKeyOf(entity.getMetadata()
                        .getArn())
                .descriptionOf(entity.getMetadata()
                        .getDescription())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                            EdsAsset edsAsset, AliyunKms.KmsKey entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, ALIYUN_KMS_ENDPOINT, entity.getEndpoint()));
        indices.add(createEdsAssetIndex(edsAsset, ALIYUN_KMS_INSTANCE_ID, entity.getMetadata()
                .getDKMSInstanceId()));
        return indices;
    }

}