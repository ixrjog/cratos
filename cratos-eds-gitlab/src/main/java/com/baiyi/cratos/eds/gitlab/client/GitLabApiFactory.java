package com.baiyi.cratos.eds.gitlab.client;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsGitLabConfigModel;
import lombok.NoArgsConstructor;
import org.gitlab4j.api.GitLabApi;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/21 11:22
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class GitLabApiFactory {

    // Set the connect timeout to 1 second and the read timeout to 5 seconds
    private static final int CONNECT_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 5000;

    public static GitLabApi buildGitLabApi(EdsConfigs.GitLab gitLab) {
        assert gitLab != null;
        final String version = Optional.of(gitLab)
                .map(EdsConfigs.GitLab::getApi)
                .map(EdsGitLabConfigModel.Api::getVersion)
                .orElse("V4");
        GitLabApi.ApiVersion apiVersion = GitLabApi.ApiVersion.valueOf(version.toUpperCase());
        GitLabApi gitLabApi = new GitLabApi(apiVersion, gitLab.getApi()
                .getUrl(), gitLab.getCred()
                .getToken());
        int connectTimeout = Optional.of(gitLab)
                .map(EdsConfigs.GitLab::getApi)
                .map(EdsGitLabConfigModel.Api::getConnectTimeout)
                .orElse(CONNECT_TIMEOUT);
        int readTimeout = Optional.of(gitLab)
                .map(EdsConfigs.GitLab::getApi)
                .map(EdsGitLabConfigModel.Api::getReadTimeout)
                .orElse(READ_TIMEOUT);
        gitLabApi.setRequestTimeout(connectTimeout, readTimeout);
        return gitLabApi;
    }

}
