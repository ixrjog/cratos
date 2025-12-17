package com.baiyi.cratos.eds.aliyun.provider.kms;

import com.aliyun.sdk.service.kms20160120.models.GetSecretValueResponseBody;
import com.aliyun.sdk.service.kms20160120.models.ListSecretsResponseBody;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.InfoSummaryUtils;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.eds.aliyun.model.AliyunKms;
import com.baiyi.cratos.eds.aliyun.repo.AliyunKmsRepo;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseHasEndpointsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TagService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_KMS_ENDPOINT;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CONTENT_HASH;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/9 10:17
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_KMS_SECRET)
public class EdsAliyunKmsSecretAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsConfigs.Aliyun, AliyunKms.KmsSecret> {

    private final TagService tagService;
    private final BusinessTagFacade businessTagFacade;

    public EdsAliyunKmsSecretAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                           CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                           EdsAssetIndexFacade edsAssetIndexFacade,
                                           AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                           EdsInstanceProviderHolderBuilder holderBuilder, TagService tagService,
                                           BusinessTagFacade businessTagFacade) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
        this.tagService = tagService;
        this.businessTagFacade = businessTagFacade;
    }

    private static final SysTagKeys[] SECRET_TAGS = {SysTagKeys.ENV, SysTagKeys.CREATED_BY};

    @Override
    protected Set<String> listEndpoints(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        return Sets.newHashSet(Optional.of(instance)
                .map(ExternalDataSourceInstance::getConfig)
                .map(EdsConfigs.Aliyun::getKms)
                .map(EdsAliyunConfigModel.KMS::getEndpoints)
                .orElse(List.of()));
    }

    @Override
    protected List<AliyunKms.KmsSecret> listEntities(String endpoint,
                                                     ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        List<ListSecretsResponseBody.Secret> secrets = AliyunKmsRepo.listSecrets(endpoint,
                instance.getConfig());
        if (CollectionUtils.isEmpty(secrets)) {
            return List.of();
        }
        return toKmsSecret(endpoint, instance.getConfig(), secrets);
    }

    private List<AliyunKms.KmsSecret> toKmsSecret(String endpoint, EdsConfigs.Aliyun configModel,
                                                  List<ListSecretsResponseBody.Secret> secrets) {
        return secrets.stream()
                .map(e -> AliyunKmsRepo.describeSecret(endpoint, configModel, e.getSecretName())
                        .map(response -> {
                            AliyunKms.SecretMetadata metadata = BeanCopierUtils.copyProperties(response,
                                    AliyunKms.SecretMetadata.class);
                            metadata.setTags(AliyunKms.Tags.of(response.getTags()));
                            return AliyunKms.KmsSecret.builder()
                                    .endpoint(endpoint)
                                    .secret(BeanCopierUtils.copyProperties(e, AliyunKms.Secret.class))
                                    .metadata(metadata)
                                    .build();
                        })
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                         AliyunKms.KmsSecret entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getSecret()
                        .getSecretName())
                .nameOf(entity.getSecret()
                        .getSecretName())
                .assetKeyOf(entity.getMetadata()
                        .getArn())
                .kindOf(entity.getMetadata()
                        .getSecretType())
                .createdTimeOf(toUtcDate(entity.getSecret()
                        .getCreateTime()))
                .descriptionOf(entity.getMetadata()
                        .getDescription())
                .build();
    }

    public static Date toUtcDate(String time) {
        return TimeUtils.toDate(time, TimeZoneEnum.UTC);
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance, EdsAsset edsAsset,
            AliyunKms.KmsSecret entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, ALIYUN_KMS_ENDPOINT, entity.getEndpoint()));
        Optional<GetSecretValueResponseBody> optionalGetSecretValueResponseBody = AliyunKmsRepo.getSecretValue(
                entity.getEndpoint(), instance.getConfig(), entity.getSecret()
                        .getSecretName());
        if (optionalGetSecretValueResponseBody.isPresent()) {
            String contentHash = InfoSummaryUtils.toContentHash(InfoSummaryUtils.SHA256, InfoSummaryUtils.toSHA256(
                    optionalGetSecretValueResponseBody.get()
                            .getSecretData()));
            indices.add(createEdsAssetIndex(edsAsset, CONTENT_HASH, contentHash));
        }
        return indices;
    }

    @Override
    protected EdsAsset saveEntityAsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                         AliyunKms.KmsSecret entity) {
        EdsAsset asset = super.saveEntityAsAsset(instance, entity);
        // 获取符合条件的标签资源
        List<AliyunKms.Tag> tags = Optional.of(entity)
                .map(AliyunKms.KmsSecret::getMetadata)
                .map(AliyunKms.SecretMetadata::getTags)
                .map(AliyunKms.Tags::getTag)
                .orElse(List.of())
                .stream()
                .filter(tag -> Arrays.stream(SECRET_TAGS)
                        .anyMatch(tagKey -> tagKey.getKey()
                                .equals(tag.getTagKey())))
                .toList();
        if (CollectionUtils.isEmpty(tags)) {
            return asset;
        }
        // 保存业务标签
        tags.stream()
                .map(tagResource -> {
                    Tag tag = tagService.getByTagKey(tagResource.getTagKey());
                    if (Objects.isNull(tag)) {
                        return null;
                    }
                    return BusinessTagParam.SaveBusinessTag.builder()
                            .businessId(asset.getId())
                            .businessType(BusinessTypeEnum.EDS_ASSET.name())
                            .tagId(tag.getId())
                            .tagValue(tagResource.getTagValue())
                            .build();
                })
                .filter(Objects::nonNull)
                .forEach(businessTagFacade::saveBusinessTag);
        return asset;
    }

}