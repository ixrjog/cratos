package com.baiyi.cratos.eds.gitlab.facade;

import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.gitlab.repo.GitLabProjectRepo;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/28 10:32
 * &#064;Version 1.0
 */
@Component
public class GitLabProjectFacade {

    @Retryable(retryFor = GitLabApiException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void addProjectMember(EdsGitLabConfigModel.GitLab gitLab, Long projectId, Long userId,
                                 AccessLevel accessLevel) throws GitLabApiException {
        GitLabProjectRepo.addMember(gitLab, projectId, userId, accessLevel);
    }

    @Retryable(retryFor = GitLabApiException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<Member> getProjectMembers(EdsGitLabConfigModel.GitLab gitLab,
                                          Long projectId) throws GitLabApiException {
        return GitLabProjectRepo.getMembersWithProjectId(gitLab, projectId);
    }

    @Retryable(retryFor = GitLabApiException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void updateProjectMember(EdsGitLabConfigModel.GitLab gitLab, Long projectId, Long userId,
                                    AccessLevel accessLevel) throws GitLabApiException {
        GitLabProjectRepo.updateMember(gitLab, projectId, userId, accessLevel);
    }

}
