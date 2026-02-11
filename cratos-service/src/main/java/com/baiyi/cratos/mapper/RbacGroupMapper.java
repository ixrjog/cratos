package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.param.http.rbac.RbacGroupParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RbacGroupMapper extends Mapper<RbacGroup> {

    List<RbacGroup> queryPageByParam(RbacGroupParam.GroupPageQuery pageQuery);

}