package com.baiyi.cratos.eds.gitlab.client;

import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import org.gitlab4j.api.GitLabApi;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/3/21 11:22
 * @Version 1.0
 */
public class GitLabApiFactory {

    // Set the connect timeout to 1 second and the read timeout to 5 seconds
    private static final int CONNECT_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 5000;

    public static GitLabApi buildGitLabApi(EdsGitLabConfigModel.GitLab gitlab) {
        assert gitlab != null;

        GitLabApi.ApiVersion apiVersion = GitLabApi.ApiVersion.valueOf(Optional.of(gitlab)
                .map(EdsGitLabConfigModel.GitLab::getApi)
                .map(EdsGitLabConfigModel.Api::getVersion)
                .orElse("v4"));

        GitLabApi gitLabApi = new GitLabApi(apiVersion, gitlab.getUrl(), gitlab.getCred()
                .getToken());
        int connectTimeout = Optional.of(gitlab)
                .map(EdsGitLabConfigModel.GitLab::getApi)
                .map(EdsGitLabConfigModel.Api::getConnectTimeout)
                .orElse(CONNECT_TIMEOUT);
        int readTimeout = Optional.of(gitlab)
                .map(EdsGitLabConfigModel.GitLab::getApi)
                .map(EdsGitLabConfigModel.Api::getReadTimeout)
                .orElse(READ_TIMEOUT);
        gitLabApi.setRequestTimeout(connectTimeout, readTimeout);
        return gitLabApi;
    }

}
