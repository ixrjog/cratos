package com.baiyi.cratos.facade.rbac;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.rbac.RbacGroupParam;
import com.baiyi.cratos.domain.view.rbac.RbacGroupVO;

/**
 * @Author baiyi
 * @Date 2024/1/17 10:07
 * @Version 1.0
 */
public interface RbacGroupFacade {

    DataTable<RbacGroupVO.Group> queryGroupPage(RbacGroupParam.GroupPageQuery pageQuery);

}
