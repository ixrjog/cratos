package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserPermissionMapper extends Mapper<UserPermission> {

    List<UserPermission> queryPageByParam(UserPermissionParam.UserPermissionPageQuery pageQuery);

    List<Integer> queryUserPermissionBusinessIds(@Param("username") String username, @Param("businessType") String businessType);

}