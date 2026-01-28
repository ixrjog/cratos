package com.baiyi.cratos.eds.gitlab.facade;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.gitlab.repo.GitLabUserRepo;
import com.baiyi.cratos.service.UserService;
import lombok.AllArgsConstructor;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.User;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/28 10:12
 * &#064;Version 1.0
 */
@Component
@AllArgsConstructor
public class GitLabUserFacade {

    private final UserService userService;

    @Retryable(retryFor = GitLabApiException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public List<User> findUsers(EdsConfigs.GitLab gitlab, String emailOrUsername) throws GitLabApiException {
        return GitLabUserRepo.findUsers(gitlab, emailOrUsername);
    }

    @Retryable(retryFor = GitLabApiException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public User createUser(EdsConfigs.GitLab gitlab, String username) throws GitLabApiException {
        com.baiyi.cratos.domain.generator.User localUser = userService.getByUsername(username);
        User user = new User()
                .withUsername(username)
                .withName(localUser.getDisplayName())
                .withEmail(localUser.getEmail())
                // 跳过确认
                .withSkipConfirmation(true);
        // 使用随机密码
        return GitLabUserRepo.createUser(gitlab, user, PasswordGenerator.generatePassword());
    }

}
