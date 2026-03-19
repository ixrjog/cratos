package com.baiyi.cratos.eds.gitlab.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.gitlab.repo.GitLabProjectRepo;
import com.google.common.collect.Lists;
import org.gitlab4j.api.models.Project;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * @Author baiyi
 * @Date 2024/3/21 13:44
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GITLAB, assetTypeOf = EdsAssetTypeEnum.GITLAB_PROJECT)
public class EdsGitLabProjectAssetProvider extends BaseEdsAssetProvider<EdsConfigs.GitLab, Project> {

    public EdsGitLabProjectAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<Project> listEntities(
            ExternalDataSourceInstance<EdsConfigs.GitLab> instance) throws EdsQueryEntitiesException {
        try {
            return GitLabProjectRepo.getProjects(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.GitLab> instance, Project entity) {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .createdTimeOf(entity.getCreatedAt())
                .descriptionOf(entity.getDescription())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> buildIndexes(ExternalDataSourceInstance<EdsConfigs.GitLab> instance,
                                               EdsAsset edsAsset, Project entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, REPO_SSH_URL, entity.getSshUrlToRepo()));
        indices.add(createEdsAssetIndex(edsAsset, REPO_HTTP_URL, entity.getHttpUrlToRepo()));
        indices.add(createEdsAssetIndex(edsAsset, REPO_WEB_URL, entity.getWebUrl()));
        return indices;
    }

    @Override
    protected boolean isAssetUnchanged(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.builder()
                .comparisonName(true)
                .build()
                .compare(a1, a2);
    }

}