package com.baiyi.cratos.eds.alimail.repo;

import com.baiyi.cratos.eds.alimail.model.AlimailDepartment;
import com.baiyi.cratos.eds.alimail.service.AlimailService;
import com.baiyi.cratos.eds.alimail.service.AlimailServiceFactory;
import com.baiyi.cratos.eds.core.config.EdsAlimailConfigModel;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 16:29
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlimailDepartmentRepo {

    public static final String ROOT = "$root";

    /**
     * 查询部门下的子部门
     *
     * @param alimail
     * @param id      部门ID
     * @return
     */
    public static List<AlimailDepartment.Department> listSubDepartments(EdsAlimailConfigModel.Alimail alimail, String id) {
        AlimailService alimailService = AlimailServiceFactory.createAuthenticatedService(alimail);
        List<AlimailDepartment.Department> result = Lists.newArrayList();
        int offset = 0;
        final int limit = 100;
        AlimailDepartment.ListSubDepartmentsResult dept;
        do {
            dept = alimailService.listSubDepartments(id, limit, offset);
            if (!CollectionUtils.isEmpty(dept.getDepartments())) {
                result.addAll(dept.getDepartments());
                offset += limit;
            }
        } while (!CollectionUtils.isEmpty(dept.getDepartments()));
        return result;
    }

    public static List<AlimailDepartment.Department> listSubDepartments(EdsAlimailConfigModel.Alimail alimail,
                                                                 List<AlimailDepartment.Department> departments) {
        List<AlimailDepartment.Department> result = Lists.newArrayList();
        List<AlimailDepartment.Department> stack = Lists.newArrayList(departments); // 使用显式栈代替递归
        while (!stack.isEmpty()) {
            AlimailDepartment.Department current = stack.removeLast();
            List<AlimailDepartment.Department> subDepartments = listSubDepartments(alimail, current.getId());
            if (!CollectionUtils.isEmpty(subDepartments)) {
                result.addAll(subDepartments);
                stack.addAll(subDepartments); // 将子部门加入栈中
            }
        }
        return result;
    }

}
