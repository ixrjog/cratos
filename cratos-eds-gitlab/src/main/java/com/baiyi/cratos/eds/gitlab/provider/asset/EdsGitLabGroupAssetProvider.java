package com.baiyi.cratos.eds.gitlab.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.gitlab.repo.GitLabGroupRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
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
public class EdsGitLabGroupAssetProvider extends BaseEdsInstanceAssetProvider<EdsGitLabConfigModel.GitLab, Group> {

    public EdsGitLabGroupAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                       CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                       EdsAssetIndexFacade edsAssetIndexFacade,
                                       UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                       EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<Group> listEntities(
            ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance) throws EdsQueryEntitiesException {
        try {
            return GitLabGroupRepo.getGroups(instance.getEdsConfigModel());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance, Group entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getFullName())
                .assetKeyOf(entity.getFullPath())
                .createdTimeOf(entity.getCreatedAt())
                .descriptionOf(entity.getDescription())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance,
                                                      EdsAsset edsAsset, Group entity) {
        return List.of(toEdsAssetIndex(edsAsset, REPO_WEB_URL, entity.getWebUrl()));
    }

}