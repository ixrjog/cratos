package com.baiyi.cratos.eds.aliyun.provider.kms;

import com.aliyun.sdk.service.kms20160120.models.ListSecretsResponseBody;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
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

import java.util.*;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_KMS_ENDPOINT;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/9 10:17
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_KMS_SECRET)
public class EdsAliyunKmsSecretAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret> {

    public EdsAliyunKmsSecretAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
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
    protected List<AliyunKms.KmsSecret> listEntities(String endpoint,
                                                     ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        List<ListSecretsResponseBody.Secret> secrets = AliyunKmsRepo.listSecrets(endpoint,
                instance.getEdsConfigModel());
        if (CollectionUtils.isEmpty(secrets)) {
            return List.of();
        }
        return toKmsSecret(endpoint, instance.getEdsConfigModel(), secrets);
    }

    private List<AliyunKms.KmsSecret> toKmsSecret(String endpoint, EdsAliyunConfigModel.Aliyun configModel,
                                                  List<ListSecretsResponseBody.Secret> secrets) {
        return secrets.stream()
                .map(e -> AliyunKmsRepo.describeSecret(endpoint, configModel, e.getSecretName())
                        .map(response -> AliyunKms.KmsSecret.builder()
                                .endpoint(endpoint)
                                .secret(BeanCopierUtil.copyProperties(e, AliyunKms.Secret.class))
                                .metadata(BeanCopierUtil.copyProperties(response, AliyunKms.SecretMetadata.class))
                                .build())
                        .orElseGet(() -> AliyunKms.KmsSecret.builder()
                                .endpoint(endpoint)
                                .secret(BeanCopierUtil.copyProperties(e, AliyunKms.Secret.class))
                                .metadata(AliyunKms.SecretMetadata.NO_DATA)
                                .build()))
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunKms.KmsSecret entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getSecret()
                        .getSecretName())
                .nameOf(entity.getSecret()
                        .getSecretName())
                .assetKeyOf(entity.getMetadata()
                        .getArn())
                .kindOf(entity.getMetadata().getSecretType())
                .createdTimeOf(toUtcDate(entity.getSecret().getCreateTime()))
                .build();
    }

    public static Date toUtcDate(String time) {
        return TimeUtils.toDate(time, TimeZoneEnum.UTC);
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                                      EdsAsset edsAsset, AliyunKms.KmsSecret entity) {
        return List.of(toEdsAssetIndex(edsAsset, ALIYUN_KMS_ENDPOINT, entity.getEndpoint()));
    }

}