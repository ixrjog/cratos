package com.baiyi.cratos.eds.dingtalk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2021/11/29 6:03 下午
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class DingtalkDepartment {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DepartmentSubIdResult extends DingtalkResult.Query implements Serializable {

        @Serial
        private static final long serialVersionUID = 8776879385196041038L;
        private Result result;
    }

    @Data
    public static class Result implements Serializable {

        @Serial
        private static final long serialVersionUID = -7264362835671584691L;
        @JsonProperty("dept_id_list")
        private List<Long> deptIdList;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetDepartmentResult extends DingtalkResult.Query implements Serializable {

        @Serial
        private static final long serialVersionUID = 536273783194486923L;
        private Department result;
    }

    /**
     * https://developers.dingtalk.com/document/app/query-department-details0-v2
     */
    @Data
    public static class Department implements Serializable {

        @Serial
        private static final long serialVersionUID = -5297487050997934333L;

        @JsonProperty("dept_id")
        private Long deptId;
        private String name;
        @JsonProperty("parent_id")
        private Long parentId;
        @JsonProperty("hide_dept")
        private Boolean hideDept; // 是否隐藏本部门

    }

}