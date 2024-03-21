package com.baiyi.cratos.eds.gitlab.client;

import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import org.gitlab4j.api.GitLabApi;

/**
 * @Author baiyi
 * @Date 2024/3/21 11:21
 * @Version 1.0
 */
public class GitLabApiBuilder {

    private GitLabApiBuilder() {
    }

    public static GitLabApi build(EdsGitLabConfigModel.GitLab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}
