package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Project;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ProjectMapper extends Mapper<Project> {

    List<Project> queryPageByParam(ProjectParam.ProjectPageQueryParam pageQuery);

}
