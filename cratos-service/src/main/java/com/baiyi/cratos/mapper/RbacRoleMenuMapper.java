package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RbacRoleMenu;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RbacRoleMenuMapper extends Mapper<RbacRoleMenu> {

   List<Integer> queryUserMenuIds(String username);

}