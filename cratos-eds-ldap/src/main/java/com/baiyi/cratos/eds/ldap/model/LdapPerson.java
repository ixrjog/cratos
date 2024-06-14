package com.baiyi.cratos.eds.ldap.model;

import lombok.*;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:17
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class LdapPerson {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @ToString
    @Entry(objectClasses = {"inetorgperson"})
    public static class Person {
        /**
         * 主键
         */
        @Attribute
        private String personId;

        /**
         * 用户名
         */
        @Attribute(name = "cn")
        private String username;

        /**
         * 显示名
         */
        @Attribute(name = "displayName")
        private String displayName;

        /**
         * 电话
         */
        @Attribute(name = "mobile")
        private String mobile;
        /**
         * 邮箱
         */
        @Attribute(name = "mail")
        private String email;

        /**
         * 工号
         */
        @Attribute(name = "jobNo")
        private String jobNo;

        /**
         * 证件类型
         */
        @Attribute(name = "certType")
        private Integer certType;
        /**
         * 证件号码
         */
        @Attribute(name = "certificateNo")
        private String certNo;

        @Attribute(name = "userPassword")
        private String userPassword;

        @Attribute
        protected Date createTime;

        /**
         * 更新时间
         */
        @Attribute
        protected Date updateTime;

        /**
         * 状态
         */
        @Attribute
        protected Integer status;

        @Attribute
        protected Integer disOrder;

        /**
         * 工作单位
         */
        @Attribute
        private String company;

    }

}
