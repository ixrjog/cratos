package com.baiyi.cratos.eds.gitlab.provider.asset;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.param.http.gitlab.GitLabEventParam;
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
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 13:46
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GITLAB, assetTypeOf = EdsAssetTypeEnum.GITLAB_SYSTEM_HOOK)
public class EdsGitLabSystemHookAssetProvider extends BaseEdsInstanceAssetProvider<EdsGitLabConfigModel.GitLab, GitLabEventParam.SystemHook> {

    public EdsGitLabSystemHookAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                            EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<GitLabEventParam.SystemHook> listEntities(
            ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("Query not supported.");
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance,
                                  GitLabEventParam.SystemHook entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.hashCode())
                .nameOf(entity.getEventName())
                .kindOf(entity.getEventName())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> convertToEdsAssetIndexList(ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance,
                                                      EdsAsset edsAsset, GitLabEventParam.SystemHook entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        Optional.ofNullable(entity.getUserId())
                .filter(IdentityUtils::hasIdentity)
                .ifPresent(userId -> indices.add(createEdsAssetIndex(edsAsset, GITLAB_USER_ID, userId)));
        Optional.ofNullable(entity.getProjectId())
                .filter(IdentityUtils::hasIdentity)
                .ifPresent(projectId -> indices.add(createEdsAssetIndex(edsAsset, GITLAB_PROJECT_ID, projectId)));
        Optional.ofNullable(entity.getGroupId())
                .filter(IdentityUtils::hasIdentity)
                .ifPresent(projectId -> indices.add(createEdsAssetIndex(edsAsset, GITLAB_GROUP_ID, projectId)));
        return indices;
    }

}