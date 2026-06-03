package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.ProjectGroup;
import com.baiyi.cratos.mapper.ProjectGroupMapper;
import com.baiyi.cratos.service.ProjectGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
public class ProjectGroupServiceImpl implements ProjectGroupService {

    private final ProjectGroupMapper projectGroupMapper;

    @Override
    public List<ProjectGroup> queryByProjectId(int projectId) {
        Example example = new Example(ProjectGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        return projectGroupMapper.selectByExample(example);
    }

    @Override
    public List<ProjectGroup> queryByTenantId(int tenantId) {
        Example example = new Example(ProjectGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tenantId", tenantId);
        return projectGroupMapper.selectByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:PROJECT_GROUP:ID:' + #id")
    public void clearCacheById(int id) {
    }
}
