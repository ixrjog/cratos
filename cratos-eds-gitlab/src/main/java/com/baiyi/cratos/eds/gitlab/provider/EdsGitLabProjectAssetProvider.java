package com.baiyi.cratos.eds.gitlab.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.gitlab.repo.GitLabProjectRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
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
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.GITLAB, assetType = EdsAssetTypeEnum.GITLAB_PROJECT)
public class EdsGitLabProjectAssetProvider extends BaseEdsInstanceAssetProvider<EdsGitLabConfigModel.GitLab, Project> {

    public EdsGitLabProjectAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                         CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                         EdsAssetIndexFacade edsAssetIndexFacade,
                                         UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected List<Project> listEntities(
            ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance) throws EdsQueryEntitiesException {
        try {
            return GitLabProjectRepo.getProjects(instance.getEdsConfigModel());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance, Project entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .createdTimeOf(entity.getCreatedAt())
                .descriptionOf(entity.getDescription())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance,
                                                      EdsAsset edsAsset, Project entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(toEdsAssetIndex(edsAsset, REPO_SSH_URL, entity.getSshUrlToRepo()));
        indices.add(toEdsAssetIndex(edsAsset, REPO_HTTP_URL, entity.getHttpUrlToRepo()));
        indices.add(toEdsAssetIndex(edsAsset, REPO_WEB_URL, entity.getWebUrl()));
        return indices;
    }

}