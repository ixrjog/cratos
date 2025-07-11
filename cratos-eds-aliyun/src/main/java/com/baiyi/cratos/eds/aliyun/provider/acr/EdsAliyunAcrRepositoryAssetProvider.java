package com.baiyi.cratos.eds.aliyun.provider.acr;

import com.aliyuncs.cr.model.v20181201.ListInstanceResponse;
import com.aliyuncs.cr.model.v20181201.ListRepositoryResponse;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aliyun.repo.AliyunAcrRepo;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
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
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_ACR_INSTANCE_ID;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_ACR_REPO_NAMESPACE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/25 上午10:40
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_ACR_REPOSITORY)
public class EdsAliyunAcrRepositoryAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, ListRepositoryResponse.RepositoriesItem> {

    private final AliyunAcrRepo aliyunAcrRepo;

    public EdsAliyunAcrRepositoryAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                               CredentialService credentialService,
                                               ConfigCredTemplate configCredTemplate,
                                               EdsAssetIndexFacade edsAssetIndexFacade,
                                               UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                               EdsInstanceProviderHolderBuilder holderBuilder,
                                               AliyunAcrRepo aliyunAcrRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.aliyunAcrRepo = aliyunAcrRepo;
    }

    @Override
    protected List<ListRepositoryResponse.RepositoriesItem> listEntities(String regionId,
                                                                         EdsAliyunConfigModel.Aliyun configModel) throws EdsQueryEntitiesException {
        try {
            List<ListInstanceResponse.InstancesItem> instancesItems = aliyunAcrRepo.listInstance(regionId, configModel);
            if (CollectionUtils.isEmpty(instancesItems)) {
                return Collections.emptyList();
            }
            List<ListRepositoryResponse.RepositoriesItem> entities = Lists.newArrayList();
            for (ListInstanceResponse.InstancesItem instancesItem : instancesItems) {
                List<ListRepositoryResponse.RepositoriesItem> repositoriesItems = aliyunAcrRepo.listRepository(regionId,
                        configModel, instancesItem.getInstanceId());
                if (!CollectionUtils.isEmpty(repositoriesItems)) {
                    entities.addAll(repositoriesItems);
                }
            }
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    public static Date toUtcDate(String time) {
        return TimeUtils.toDate(time, TimeZoneEnum.UTC);
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  ListRepositoryResponse.RepositoriesItem entity) {
        final String key = Joiner.on(":")
                .join(entity.getInstanceId(), entity.getRepoId());
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getRepoId())
                .nameOf(entity.getRepoName())
                .assetKeyOf(key)
                .statusOf(entity.getRepoStatus())
                .createdTimeOf(entity.getCreateTime())
                .descriptionOf(entity.getSummary())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> convertToEdsAssetIndexList(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                                      EdsAsset edsAsset,
                                                      ListRepositoryResponse.RepositoriesItem entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            indices.add(createEdsAssetIndex(edsAsset, ALIYUN_ACR_INSTANCE_ID, entity.getInstanceId()));
            indices.add(createEdsAssetIndex(edsAsset, ALIYUN_ACR_REPO_NAMESPACE, entity.getRepoNamespaceName()));
        } catch (Exception ignored) {
        }
        return indices;
    }

    @Override
    protected Set<String> getRegionSet(EdsAliyunConfigModel.Aliyun configModel) {
        return Sets.newHashSet(Optional.of(configModel)
                .map(EdsAliyunConfigModel.Aliyun::getAcr)
                .map(EdsAliyunConfigModel.ACR::getRegionIds)
                .orElse(Collections.emptyList()));
    }

}
