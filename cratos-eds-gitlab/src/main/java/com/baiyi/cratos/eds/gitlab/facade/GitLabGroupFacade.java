package com.baiyi.cratos.eds.gitlab.facade;

import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.gitlab.repo.GitLabGroupRepo;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/28 11:36
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class GitLabGroupFacade {

    @Retryable(retryFor = GitLabApiException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void addMember(EdsGitLabConfigModel.GitLab gitLab, Long groupId, Long userId,
                          AccessLevel accessLevel) throws GitLabApiException {
        GitLabGroupRepo.addMember(gitLab, groupId, userId, accessLevel);
    }

    @Retryable(retryFor = GitLabApiException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void updateMember(EdsGitLabConfigModel.GitLab gitLab, Long groupId, Long userId,
                             AccessLevel accessLevel) throws GitLabApiException {
        GitLabGroupRepo.updateMember(gitLab, groupId, userId, accessLevel);
    }

    @Retryable(retryFor = GitLabApiException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<Member> getMembers(EdsGitLabConfigModel.GitLab gitLab, Long groupId) throws GitLabApiException {
        return GitLabGroupRepo.getMembersWithGroupId(gitLab, groupId);
    }

}
