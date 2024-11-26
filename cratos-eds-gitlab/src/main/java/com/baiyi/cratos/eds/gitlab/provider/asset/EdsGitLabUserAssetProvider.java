package com.baiyi.cratos.eds.gitlab.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
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
import com.baiyi.cratos.eds.gitlab.repo.GitLabUserRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.gitlab4j.api.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/21 14:21
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GITLAB, assetTypeOf = EdsAssetTypeEnum.GITLAB_USER)
public class EdsGitLabUserAssetProvider extends BaseEdsInstanceAssetProvider<EdsGitLabConfigModel.GitLab, User> {

    public EdsGitLabUserAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                      CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                      EdsAssetIndexFacade edsAssetIndexFacade,
                                      UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected List<User> listEntities(
            ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance) throws EdsQueryEntitiesException {
        try {
            return GitLabUserRepo.getUsers(instance.getEdsConfigModel());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance, User entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .assetKeyOf(entity.getUsername())
                .validOf("active".equals(entity.getState()))
                .descriptionOf(entity.getEmail())
                .createdTimeOf(entity.getCreatedAt())
                .build();
    }

}
