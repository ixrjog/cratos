package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.ApplicationResource;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ApplicationResourceMapper extends Mapper<ApplicationResource> {

    List<String> getNamespaceOptions();

}