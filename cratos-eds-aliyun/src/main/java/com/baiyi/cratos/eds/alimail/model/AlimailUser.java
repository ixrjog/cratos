package com.baiyi.cratos.eds.alimail.model;

import lombok.Data;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/12 09:23
 * &#064;Version 1.0
 */
public class AlimailUser {

    @Data
    public static class ListUsersOfDepartmentResult {
        private List<User> users;
    }

    @Data
    public static class ResetPasswordResult {
        private String detailErrorCode;
        private String message;
    }

    @Data
    public static class User {
        private String id;
        private String email;
        private List<String> emailAliases;
        private String name;
        private String nickname;
        private String employeeNo;
        private String jobTitle;
        private String workLocation;
        private String officeLocation;
        private List<String> departmentIds;
        private String status;
        private Avatar avatar;
    }

    @Data
    public static class Avatar {
        private String url;
        private String bgColor;
    }

}
