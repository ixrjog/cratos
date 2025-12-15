package com.baiyi.cratos.eds.gitlab.client;

import com.baiyi.cratos.eds.core.config.model.EdsGitLabConfigModel;
import lombok.NoArgsConstructor;
import org.gitlab4j.api.GitLabApi;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/21 11:21
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class GitLabApiBuilder {

    public static GitLabApi build(EdsGitLabConfigModel.GitLab gitlab) {
        return GitLabApiFactory.buildGitLabApi(gitlab);
    }

}
