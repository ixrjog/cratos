package com.baiyi.cratos.eds.gitlab.repo;

import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.gitlab.client.GitLabApiBuilder;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.Pager;
import org.gitlab4j.api.models.*;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/3/21 11:43
 * @Version 1.0
 */
@Slf4j
public class GitLabProjectRepo {

    public static final int ITEMS_PER_PAGE = 20;

    /**
     * 查询项目中所有成员
     *
     * @param gitlab
     * @param projectId
     * @param itemsPerPage 分页查询长度
     * @return
     * @throws GitLabApiException
     */
    public static List<Member> getMembersByProjectId(EdsGitLabConfigModel.GitLab gitlab, Long projectId, int itemsPerPage) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            Pager<Member> memberPager = gitLabApi.getProjectApi().getMembers(projectId, itemsPerPage);
            return memberPager.all();
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 查询项目中所有成员
     *
     * @param gitlab
     * @param projectId
     * @return
     * @throws GitLabApiException
     */
    public static List<Member> getMembersByProjectId(EdsGitLabConfigModel.GitLab gitlab, Long projectId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            Pager<Member> memberPager = gitLabApi.getProjectApi().getMembers(projectId, ITEMS_PER_PAGE);
            return memberPager.all();
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 修改项目成员
     *
     * @param gitlab
     * @param projectId
     * @param userId
     * @param accessLevel
     * @throws GitLabApiException
     */
    public static void updateMember(EdsGitLabConfigModel.GitLab gitlab, Long projectId, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            gitLabApi.getProjectApi().updateMember(projectId, userId, accessLevel);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 新增项目成员
     *
     * @param gitlab
     * @param projectId
     * @param userId
     * @param accessLevel
     * @throws GitLabApiException
     */
    public static void addMember(EdsGitLabConfigModel.GitLab gitlab, Long projectId, Long userId, AccessLevel accessLevel) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            gitLabApi.getProjectApi().addMember(projectId, userId, accessLevel);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    /**
     * 查询GitLab实例中所有项目
     *
     * @param gitlab
     * @return
     * @throws GitLabApiException
     */
    public static List<Project> getProjects(EdsGitLabConfigModel.GitLab gitlab) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getProjectApi().getProjects();
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static List<Tag> getTagsByProjectId(EdsGitLabConfigModel.GitLab gitlab, Long projectId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getTagsApi().getTags(projectId);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static Optional<Tag> getTagByProjectIdAndTagName(EdsGitLabConfigModel.GitLab gitlab, Long projectId, String tagName) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getTagsApi().getOptionalTag(projectId, tagName);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static List<Branch> getBranchesByProjectId(EdsGitLabConfigModel.GitLab gitlab, Long projectId) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getRepositoryApi().getBranches(projectId);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static Optional<Branch> getBranchByProjectIdAndBranchName(EdsGitLabConfigModel.GitLab gitlab, Long projectId, String branchName) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getRepositoryApi().getOptionalBranch(projectId, branchName);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

    public static Branch createBranch(EdsGitLabConfigModel.GitLab gitlab, Long projectId, String branchName, String ref) throws GitLabApiException {
        try (GitLabApi gitLabApi = GitLabApiBuilder.build(gitlab)) {
            return gitLabApi.getRepositoryApi().createBranch(projectId, branchName, ref);
        } catch (GitLabApiException e) {
            log.debug(e.getMessage());
            throw e;
        }
    }

}
