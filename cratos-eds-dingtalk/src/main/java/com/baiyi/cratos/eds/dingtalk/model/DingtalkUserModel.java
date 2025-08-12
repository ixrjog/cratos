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
    }

}