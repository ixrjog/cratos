package com.baiyi.cratos.eds.gitlab.repo;

import com.baiyi.cratos.eds.core.config.model.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.gitlab.client.GitLabApiBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.SshKey;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/21 11:42
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class GitLabSshKeyRepo {

    /**
     * 查询用户所有SshKey
     *
     * @param gitLab
     * @param userId
     * @return
     * @throws GitLabApiException
     */
    public static List<SshKey> getSshKeysByUserId(EdsGitLabConfigModel.GitLab gitLab,
                                                  Long userId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitLab)) {
            return gitLabApi.getUserApi()
                    .getSshKeys(userId);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static SshKey getSshKey(EdsGitLabConfigModel.GitLab gitLab, Long keyId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitLab)) {
            return gitLabApi.getUserApi()
                    .getSshKey(keyId);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

}
