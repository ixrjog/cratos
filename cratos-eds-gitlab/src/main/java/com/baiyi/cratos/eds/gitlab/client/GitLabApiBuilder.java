package com.baiyi.cratos.eds.gitlab.client;


import com.baiyi.cratos.eds.core.config.EdsConfigs;
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

    public static GitLabApi build(EdsConfigs.GitLab gitLab) {
        return GitLabApiFactory.buildGitLabApi(gitLab);
    }

}
