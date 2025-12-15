package com.baiyi.cratos.eds.gitlab.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.gitlab.client.GitLabApiBuilder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.Pager;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/21 14:12
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class GitLabGroupRepo {

    public static final int ITEMS_PER_PAGE = 20;

    /**
     * 查询群组中所有成员
     *
     * @param gitLab
     * @param groupId
     * @return
     * @throws GitLabApiException
     */
    public static List<Member> getMembersByGroupId(EdsConfigs.GitLab gitLab,
                                                   Long groupId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitLab)) {
            Pager<Member> memberPager = gitLabApi.getGroupApi()
                    .getMembers(groupId, ITEMS_PER_PAGE);
            return memberPager.all();
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 查询群组中所有项目
     *
     * @param gitLab
     * @return
     * @throws GitLabApiException
     */
    public static List<Project> getProjectsByGroupId(EdsConfigs.GitLab gitLab,
                                                     Long groupId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitLab)) {
            Pager<Project> projectPager = gitLabApi.getGroupApi()
                    .getProjects(groupId, ITEMS_PER_PAGE);
            return projectPager.all();
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 修改群组成员
     *
     * @param gitLab
     * @param groupId
     * @param userId
     * @param accessLevel
     * @throws GitLabApiException
     */
    public static void updateMember(EdsConfigs.GitLab gitLab, Long groupId, Long userId,
                                    AccessLevel accessLevel) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitLab)) {
            gitLabApi.getGroupApi()
                    .updateMember(groupId, userId, accessLevel);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 新增群组成员
     *
     * @param gitLab
     * @param groupId
     * @param userId
     * @param accessLevel
     * @throws GitLabApiException
     */
    public static void addMember(EdsConfigs.GitLab gitLab, Long groupId, Long userId,
                                 AccessLevel accessLevel) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitLab)) {
            gitLabApi.getGroupApi()
                    .addMember(groupId, userId, accessLevel);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 查询GitLab实例中所有群组
     *
     * @param gitLab
     * @return
     * @throws GitLabApiException
     */
    public static List<Group> getGroups(EdsConfigs.GitLab gitLab) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitLab)) {
            return gitLabApi.getGroupApi()
                    .getGroups();
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static Group getGroup(EdsConfigs.GitLab gitLab, Long groupId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitLab)) {
            return gitLabApi.getGroupApi()
                    .getGroup(groupId);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static List<Member> getMembersWithGroupId(EdsConfigs.GitLab gitLab,
                                                     Long groupId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitLab)) {
            Pager<Member> memberPager = gitLabApi.getGroupApi()
                    .getMembers(groupId, ITEMS_PER_PAGE);
            return memberPager.all();
        } catch (GitLabApiException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}
