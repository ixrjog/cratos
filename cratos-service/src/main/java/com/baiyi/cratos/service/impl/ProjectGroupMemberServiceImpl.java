package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.ProjectGroupMember;
import com.baiyi.cratos.mapper.ProjectGroupMemberMapper;
import com.baiyi.cratos.service.ProjectGroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
public class ProjectGroupMemberServiceImpl implements ProjectGroupMemberService {

    private final ProjectGroupMemberMapper projectGroupMemberMapper;

    @Override
    public List<ProjectGroupMember> queryByGroupId(int groupId) {
        Example example = new Example(ProjectGroupMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId", groupId);
        return projectGroupMemberMapper.selectByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:PROJECT_GROUP_MEMBER:ID:' + #id")
    public void clearCacheById(int id) {
    }
}
