package com.baiyi.cratos.eds.gitlab.repo;

import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.gitlab.client.GitLabApiBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Membership;
import org.gitlab4j.api.models.User;

import java.io.File;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/21 11:33
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class GitLabUserRepo {

    /**
     * 按email或username查询用户
     *
     * @param gitlab
     * @param emailOrUsername
     * @return
     * @throws GitLabApiException
     */
    public static List<User> findUsers(EdsGitLabConfigModel.GitLab gitlab, String emailOrUsername) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getUserApi()
                    .findUsers(emailOrUsername);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 查询用户
     *
     * @param gitlab
     * @param userId
     * @return
     * @throws GitLabApiException
     */
    public static User getUser(EdsGitLabConfigModel.GitLab gitlab, Long userId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getUserApi()
                    .getUser(userId);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 查询GitLab实例中所有用户
     *
     * @param gitlab
     * @return
     * @throws GitLabApiException
     */
    public static List<User> getUsers(EdsGitLabConfigModel.GitLab gitlab) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getUserApi()
                    .getUsers();
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static List<Membership> getUserMemberships(EdsGitLabConfigModel.GitLab gitlab, Long userId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getUserApi()
                    .getMemberships(userId);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 设置用户头像
     *
     * @param gitlab
     * @param userId
     * @param avatarFile
     * @throws GitLabApiException
     */
    public static void updateUser(EdsGitLabConfigModel.GitLab gitlab, Long userId, File avatarFile) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            gitLabApi.getUserApi()
                    .setUserAvatar(userId, avatarFile);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 锁定用户
     *
     * @param gitlab
     * @param userId
     * @throws GitLabApiException
     */
    public static void blockUser(EdsGitLabConfigModel.GitLab gitlab, Long userId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            gitLabApi.getUserApi()
                    .blockUser(userId);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 解锁用户
     *
     * @param gitlab
     * @param userId
     * @throws GitLabApiException
     */
    public static void unblockUser(EdsGitLabConfigModel.GitLab gitlab, Long userId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            gitLabApi.getUserApi()
                    .unblockUser(userId);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static User createUser(EdsGitLabConfigModel.GitLab gitlab, User user, String password) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getUserApi()
                    .createUser(user, password, false);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

}
