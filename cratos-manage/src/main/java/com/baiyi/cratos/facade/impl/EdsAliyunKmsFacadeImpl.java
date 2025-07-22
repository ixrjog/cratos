package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.exception.EdsAliyunKmsException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.kms.AliyunKmsParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.aliyun.AliyunKmsVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.eds.aliyun.model.AliyunKms;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsAliyunKmsFacade;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/23 10:04
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsAliyunKmsFacadeImpl implements EdsAliyunKmsFacade {

    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService assetIndexService;
    private final TagService tagService;
    private final EdsFacade edsFacade;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final EdsInstanceService edsInstanceService;
    private final BusinessTagService businessTagService;
    private final BusinessTagFacade businessTagFacade;

    @Override
    public DataTable<AliyunKmsVO.Secret> queryAliyunKmsSecretPage(AliyunKmsParam.SecretPageQuery pageQuery) {
        EdsInstanceParam.AssetPageQuery assetPageQuery = pageQuery.toAssetPageQuery();
        assetPageQuery.setAssetType(EdsAssetTypeEnum.ALIYUN_KMS_SECRET.name());
        if (StringUtils.hasText(pageQuery.getCreatedBy())) {
            assetPageQuery.setQueryByTag(buildQueryByTag(pageQuery.getCreatedBy()));
        }
        DataTable<EdsAssetVO.Asset> table = edsFacade.queryEdsInstanceAssetPage(assetPageQuery);
        return new DataTable<>(toKmsSecrets(table.getData()), table.getTotalNum());
    }

    private BusinessTagParam.QueryByTag buildQueryByTag(String username) {
        Tag cratedByTag = tagService.getByTagKey(SysTagKeys.CREATED_BY);
        if (Objects.isNull(cratedByTag)) {
            log.error("Tag not found: {}", SysTagKeys.CREATED_BY);
            EdsAliyunKmsException.runtime("Tag not found: {}", SysTagKeys.CREATED_BY);
        }
        return BusinessTagParam.QueryByTag.builder()
                .tagId(cratedByTag.getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .tagValue(username)
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<AliyunKmsVO.Secret> toKmsSecrets(List<EdsAssetVO.Asset> assets) {
        if (CollectionUtils.isEmpty(assets)) {
            return List.of();
        }
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret>) edsInstanceProviderHolderBuilder.newHolder(
                assets.getFirst()
                        .getInstanceId(), EdsAssetTypeEnum.ALIYUN_KMS_SECRET.name());
        List<AliyunKmsVO.Secret> secrets = Lists.newArrayList();
        Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Maps.newHashMap();
        Map<String, EdsAssetVO.Asset> kmsInstanceMap = Maps.newHashMap();


        Tag envTag = tagService.getByTagKey(SysTagKeys.ENV);
        Tag createdByTag = tagService.getByTagKey(SysTagKeys.CREATED_BY);
        assets.forEach(asset -> {
            // Eds Instance
            EdsInstanceVO.EdsInstance instance = instanceMap.computeIfAbsent(asset.getInstanceId(),
                    a -> getInstance(asset));
            instanceMap.put(asset.getInstanceId(), instance);
            // KMS Instance
            EdsAssetVO.Asset kmsInstance = kmsInstanceMap.computeIfAbsent(asset.getAssetId(),
                    a -> getKmsInstance(asset.getInstanceId(), asset.getAssetId()));
            AliyunKms.KmsSecret kmsSecret = holder.getProvider()
                    .assetLoadAs(asset.getOriginalModel());
            AliyunKms.SecretMetadata metadata = kmsSecret.getMetadata();
            AliyunKmsVO.Secret secret = AliyunKmsVO.Secret.builder()
                    .edsInstance(instance)
                    .kmsInstance(kmsInstance)
                    .kmsInstanceId(metadata.getDKMSInstanceId())
                    .arn(metadata.getArn())
                    .secretType(metadata.getSecretType())
                    .secretName(metadata.getSecretName())
                    .description(metadata.getDescription())
                    .encryptionKeyId(metadata.getEncryptionKeyId())
                    .businessTags(getBusinessTags(asset))
                    .build();
            secrets.add(secret);
        });
        return secrets;
    }

    private List<BusinessTagVO.BusinessTag> getBusinessTags(EdsAssetVO.Asset asset) {
        BusinessParam.GetByBusiness getByBusiness = BusinessParam.GetByBusiness.builder()
                .businessId(asset.getBusinessId())
                .businessType(asset.getBusinessType())
                .build();
        return businessTagFacade.getBusinessTagByBusiness(getByBusiness);
    }

    private EdsInstanceVO.EdsInstance getInstance(EdsAssetVO.Asset asset) {
        EdsInstance instance = edsInstanceService.getById(asset.getInstanceId());
        if (Objects.isNull(instance)) {
            EdsAliyunKmsException.runtime("Eds instance not found for asset: {}", asset.getId());
        }
        return BeanCopierUtils.copyProperties(instance, EdsInstanceVO.EdsInstance.class);
    }

    private EdsAssetVO.Asset getKmsInstance(Integer instanceId, String kmsInstanceId) {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssetsById(instanceId,
                EdsAssetTypeEnum.ALIYUN_KMS_INSTANCE.name(), kmsInstanceId);

        if (CollectionUtils.isEmpty(assets)) {
            return null;
        }
        return BeanCopierUtils.copyProperties(assets.getFirst(), EdsAssetVO.Asset.class);
    }

}
