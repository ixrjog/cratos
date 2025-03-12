package com.baiyi.cratos.eds.alimail.model;

import com.baiyi.cratos.domain.constant.Global;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 16:12
 * &#064;Version 1.0
 */
public class AlimailDepartment {

    @Data
    public static class ListSubDepartmentsResult {
        private List<Department> departments;
        private Integer total;
    }

    @Data
    public static class Department {
        // ISO8601
        private String id;
        private String name;
        private String parentId;
        private Boolean hasUsers;
        private Boolean hasSubDepartments;
        @JsonFormat(pattern = Global.ISO8601)
        private Date createTime;
        private Boolean canManage;
        private List<String> hiddenExcludeUsers;
        private List<String> hiddenExcludeSubDepartments;
        private Boolean isHidden;
        private List<String> managers;
        private String email;
    }

}
