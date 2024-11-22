package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.param.http.rbac.RbacGroupParam;
import com.baiyi.cratos.domain.view.rbac.RbacGroupVO;
import com.baiyi.cratos.facade.rbac.RbacGroupFacade;
import com.baiyi.cratos.service.RbacGroupService;
import com.baiyi.cratos.wrapper.RbacGroupWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/17 10:10
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacGroupFacadeImpl implements RbacGroupFacade {

    private final RbacGroupService rbacGroupService;
    private final RbacGroupWrapper rbacGroupWrapper;

    @Override
    public DataTable<RbacGroupVO.Group> queryGroupPage(RbacGroupParam.GroupPageQuery pageQuery) {
        DataTable<RbacGroup> table = rbacGroupService.queryPageByParam(pageQuery);
        return rbacGroupWrapper.wrapToTarget(table);
    }

    @Override
    public void updateGroup(RbacGroupParam.UpdateGroup updateGroup) {
        RbacGroup rbacGroup = rbacGroupService.getById(updateGroup.getId());
        if (rbacGroup != null) {
            rbacGroup.setBase(updateGroup.getBase());
            rbacGroup.setComment(updateGroup.getComment());
            rbacGroupService.updateByPrimaryKey(rbacGroup);
        }
    }

}
