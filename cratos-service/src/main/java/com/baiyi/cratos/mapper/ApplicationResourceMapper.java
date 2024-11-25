package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.ApplicationResource;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ApplicationResourceMapper extends Mapper<ApplicationResource> {

    List<String> getNamespaceOptions();

}