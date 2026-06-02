package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.ProjectTenant;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ProjectTenantMapper extends Mapper<ProjectTenant> {

    List<ProjectTenant> queryPageByParam(ProjectParam.ProjectTenantPageQueryParam pageQuery);

}
