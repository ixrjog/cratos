package com.baiyi.cratos.eds.dingtalk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2021/11/29 4:20 下午
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class DingtalkUserModel {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResult extends DingtalkResponseModel.Query implements Serializable {
        @Serial
        private static final long serialVersionUID = 1310342064103970801L;
        private Result result;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Result extends DingtalkResponseModel.Result implements Serializable {
        @Serial
        private static final long serialVersionUID = 1969240191452753519L;
        private List<User> list;
    }

    @Data
    public static class User implements Serializable {
        @Serial
        private static final long serialVersionUID = -2450600254937294425L;
        private String username; // 转换类处理
        @JsonProperty("dept_order")
        private Long deptOrder;
        private Boolean leader;
        private String extension;
        private String unionid;
        private Boolean boss;
        @JsonProperty("exclusive_account")
        private Boolean exclusiveAccount;
        private String mobile;
        private Boolean active;
        private Boolean admin;
        private String telephone;
        private String remark;
        private String avatar;
        @JsonProperty("hide_mobile")
        private Boolean hideMobile;
        private String title;
        @JsonProperty("hired_date")
        private Date hiredDate;
        private String userid;
        @JsonProperty("org_email")
        private String orgEmail;
        private String name;
        @JsonProperty("dept_id_list")
        private List<Long> deptIdList;
        @JsonProperty("job_number")
        private String jobNumber;
        @JsonProperty("state_code")
        private String stateCode;
        private String email;
        // 需要get接口查询
        @JsonProperty("manager_userid")
        private String managerUserid;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetUserResult extends DingtalkResponseModel.Query implements Serializable {
        @Serial
        private static final long serialVersionUID = 7648704300468843547L;
        private GetUser result;
    }

    @Data
    public static class GetUser implements Serializable {
        @Serial
        private static final long serialVersionUID = -3993918356124963355L;
        private String extension;
        private String unionid;
        private String boss;
        @JsonProperty("role_list")
        private RoleList roleList;
        @JsonProperty("exclusive_account")
        private Boolean exclusiveAccount;
        @JsonProperty("manager_userid")
        private String managerUserid;
        private String admin;
        private String remark;
        private String title;
        @JsonProperty("hired_date")
        private String hiredDate;
        private String userid;
        @JsonProperty("work_place")
        private String workPlace;
        @JsonProperty("dept_order_list")
        private List<DeptOrderList> deptOrderList;
        @JsonProperty("real_authed")
        private String realAuthed;
        @JsonProperty("dept_id_list")
        private List<Long> deptIdList;
        @JsonProperty("job_number")
        private String jobNumber;
        private String email;
        @JsonProperty("leader_in_dept")
        private List<LeaderInDept> leaderInDept;
        private String mobile;
        private String active;
        @JsonProperty("org_email")
        private String orgEmail;
        private String telephone;
        private String avatar;
        @JsonProperty("hide_mobile")
        private String hideMobile;
        private String senior;
        private String name;
        @JsonProperty("union_emp_ext")
        private UnionEmpExt unionEmpExt;
        @JsonProperty("state_code")
        private String stateCode;
    }
    
    @Data
    public static class RoleList implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        @JsonProperty("group_name")
        private String groupName;
        
        private String name;
        
        private String id;
    }
    
    @Data
    public static class DeptOrderList implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        @JsonProperty("dept_id")
        private String deptId;
        
        private String order;
    }
    
    @Data
    public static class LeaderInDept implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        private String leader;
        
        @JsonProperty("dept_id")
        private String deptId;
    }
    
    @Data
    public static class UnionEmpExt implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        @JsonProperty("union_emp_map_list")
        private UnionEmpMapList unionEmpMapList;
        
        private String userid;
        
        @JsonProperty("corp_id")
        private String corpId;
    }
    
    @Data
    public static class UnionEmpMapList implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        
        private String userid;
        
        @JsonProperty("corp_id")
        private String corpId;
    }

}