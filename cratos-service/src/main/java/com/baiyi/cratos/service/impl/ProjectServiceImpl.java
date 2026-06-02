package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Project;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import com.baiyi.cratos.mapper.ProjectMapper;
import com.baiyi.cratos.service.ProjectService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:PROJECT:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public Project getByProjectKey(String projectKey) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("key", projectKey);
        return projectMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<Project> queryProjectPage(ProjectParam.ProjectPageQueryParam param) {
        Page<Project> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<Project> data = projectMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

}
