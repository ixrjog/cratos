package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.ProjectLoadBalancer;
import com.baiyi.cratos.mapper.ProjectLoadBalancerMapper;
import com.baiyi.cratos.service.ProjectLoadBalancerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
public class ProjectLoadBalancerServiceImpl implements ProjectLoadBalancerService {

    private final ProjectLoadBalancerMapper projectLoadBalancerMapper;

    @Override
    public List<ProjectLoadBalancer> queryByProjectId(int projectId) {
        Example example = new Example(ProjectLoadBalancer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        return projectLoadBalancerMapper.selectByExample(example);
    }

    @Override
    public List<ProjectLoadBalancer> queryByTenantId(int tenantId) {
        Example example = new Example(ProjectLoadBalancer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tenantId", tenantId);
        return projectLoadBalancerMapper.selectByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:PROJECT_LB:ID:' + #id")
    public void clearCacheById(int id) {
    }
}
