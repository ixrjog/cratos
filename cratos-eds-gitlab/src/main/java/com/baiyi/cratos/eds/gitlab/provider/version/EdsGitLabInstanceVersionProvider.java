package com.baiyi.cratos.eds.gitlab.provider.version;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.version.IEdsInstanceVersionProvider;
import com.baiyi.cratos.eds.gitlab.repo.version.GitLabVersionRepo;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Version;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 16:31
 * &#064;Version 1.0
 */

@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GITLAB)
public class EdsGitLabInstanceVersionProvider implements IEdsInstanceVersionProvider<EdsGitLabConfigModel.GitLab> {

    @Override
    public String getVersion(ExternalDataSourceInstance<EdsGitLabConfigModel.GitLab> instance) {
        try {
            Version version = GitLabVersionRepo.getVersion(instance.getEdsConfigModel());
            return version.getVersion();
        } catch (GitLabApiException e) {
            return "--";
        }
    }

}