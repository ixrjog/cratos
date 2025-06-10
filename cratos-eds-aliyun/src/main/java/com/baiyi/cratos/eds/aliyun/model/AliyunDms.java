package com.baiyi.cratos.eds.aliyun.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/10 10:39
 * &#064;Version 1.0
 */
public class AliyunDms {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User implements Serializable {
        @Serial
        private static final long serialVersionUID = -5958586839232632679L;
        public Long curExecuteCount;
        public Long curResultCount;
        public String dingRobot;
        public String email;
        public String lastLoginTime;
        public Long maxExecuteCount;
        public Long maxResultCount;
        public String mobile;
        public String nickName;
        public String notificationMode;
        public String parentUid;
        public String signatureMethod;
        public String state;
        public String uid;
        public String userId;
        public String webhook;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tenant implements Serializable {
        @Serial
        private static final long serialVersionUID = -1573089880798825964L;
        public String status;
        public String tenantName;
        public Long tid;
    }

}
