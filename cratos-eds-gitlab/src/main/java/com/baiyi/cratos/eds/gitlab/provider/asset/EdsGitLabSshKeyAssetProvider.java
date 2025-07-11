package com.baiyi.cratos.eds.gitlab.provider.asset;

import com.baiyi.cratos.common.util.SshKeyUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
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
import com.baiyi.cratos.eds.gitlab.data.SshKeyData;
import com.baiyi.cratos.eds.gitlab.repo.GitLabSshKeyRepo;
import com.baiyi.cratos.eds.gitlab.repo.GitLabUserRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.SshKey;
import org.gitlab4j.api.models.User;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author baiyi
 * @Date 2024/3/21 14:26
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GITLAB, assetTypeOf = EdsAssetTypeEnum.GITLAB_SSHKEY)
public class EdsGitLabSshKeyAssetProvider extends BaseEdsInstanceAssetProvider<EdsGitLabConfigModel.GitLab, SshKeyData> {

    public EdsGitLabSshKeyAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                        CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                        EdsAssetIndexFacade edsAssetIndexFacade,
                                        UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                        EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<SshKeyData> listEntities(
            ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance) throws EdsQueryEntitiesException {
        try {
            List<EdsAsset> edsUserAssets = queryAssetsByInstanceAndType(instance, EdsAssetTypeEnum.GITLAB_USER);
            if (!CollectionUtils.isEmpty(edsUserAssets)) {
                return listSshKeyWithEdsUserAssets(instance, edsUserAssets);
            }
            return listSshKeyFromRepo(instance);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    // 从EdsAsset中查询用户
    private List<SshKeyData> listSshKeyWithEdsUserAssets(
            ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance,
            List<EdsAsset> edsUserAssets) throws GitLabApiException {
        return edsUserAssets.stream()
                .flatMap(edsUserAsset -> {
                    try {
                        return GitLabSshKeyRepo.getSshKeysByUserId(instance.getEdsConfigModel(),
                                        Long.parseLong(edsUserAsset.getAssetId()))
                                .stream()
                                .map(key -> SshKeyData.builder()
                                        .sshKey(key)
                                        .username(edsUserAsset.getAssetKey())
                                        .build());
                    } catch (GitLabApiException e) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }

    private List<SshKeyData> listSshKeyFromRepo(
            ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance) throws GitLabApiException {
        List<User> users = GitLabUserRepo.getUsers(instance.getEdsConfigModel());
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream()
                .flatMap(user -> {
                    try {
                        return GitLabSshKeyRepo.getSshKeysByUserId(instance.getEdsConfigModel(), user.getId())
                                .stream()
                                .map(key -> SshKeyData.builder()
                                        .sshKey(key)
                                        .username(user.getUsername())
                                        .build());
                    } catch (GitLabApiException e) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance,
                                         SshKeyData entity) {
        SshKey sshKey = entity.getSshKey();
        return newEdsAssetBuilder(instance, entity).assetIdOf(sshKey.getId())
                .nameOf(entity.getUsername())
                .assetKeyOf(SshKeyUtils.calcFingerprint(sshKey.getKey()))
                .descriptionOf(sshKey.getTitle())
                .build();
    }

}