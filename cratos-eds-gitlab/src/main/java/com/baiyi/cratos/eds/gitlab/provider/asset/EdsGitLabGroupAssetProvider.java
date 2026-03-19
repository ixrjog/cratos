package com.baiyi.cratos.eds.gitlab.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.gitlab.repo.GitLabGroupRepo;
import org.gitlab4j.api.models.Group;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.REPO_WEB_URL;

/**
 * @Author baiyi
 * @Date 2024/3/21 14:11
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GITLAB, assetTypeOf = EdsAssetTypeEnum.GITLAB_GROUP)
public class EdsGitLabGroupAssetProvider extends BaseEdsAssetProvider<EdsConfigs.GitLab, Group> {

    public EdsGitLabGroupAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<Group> listEntities(
            ExternalDataSourceInstance<EdsConfigs.GitLab> instance) throws EdsQueryEntitiesException {
        try {
            return GitLabGroupRepo.getGroups(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.GitLab> instance, Group entity) {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getFullName())
                .assetKeyOf(entity.getFullPath())
                .createdTimeOf(entity.getCreatedAt())
                .descriptionOf(entity.getDescription())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> buildIndexes(ExternalDataSourceInstance<EdsConfigs.GitLab> instance,
                                               EdsAsset edsAsset, Group entity) {
        return List.of(createEdsAssetIndex(edsAsset, REPO_WEB_URL, entity.getWebUrl()));
    }

}