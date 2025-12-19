package com.baiyi.cratos.eds.domain.godaddy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/5 下午3:11
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class GodaddyDomain {

    private final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Domain implements Serializable {
        @Serial
        private static final long serialVersionUID = 1090915503742048104L;
        @JsonFormat(pattern = DATE_FORMAT)
        private Date createdAt;
        private String domain;
        private Long domainId;
        private Boolean expirationProtected;
        @JsonFormat(pattern = DATE_FORMAT)
        private Date expiresAt;
        private Boolean holdRegistrar;
        private Boolean locked;
        @JsonFormat(pattern = DATE_FORMAT)
        private Date modifiedAt;
        private List<String> nameServers;
        private Boolean privacy;
        private Redemption redemption;
        @JsonFormat(pattern = DATE_FORMAT)
        private Date registrarCreatedAt;
        private Boolean renewAuto;
        @JsonFormat(pattern = DATE_FORMAT)
        private Date renewDeadline;
        private String status;
        private Boolean transferProtected;
        private Renewal renewal;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Redemption implements Serializable {
        @Serial
        private static final long serialVersionUID = 6443627007966917952L;
        private Boolean redeemable;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Renewal implements Serializable {
        @Serial
        private static final long serialVersionUID = -7393512900207013405L;
        private String currency;
        private Integer price;
        private Boolean renewable;
    }

}
