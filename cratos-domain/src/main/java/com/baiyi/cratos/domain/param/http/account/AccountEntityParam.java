package com.baiyi.cratos.domain.param.http.account;

import com.baiyi.cratos.domain.generator.AccountEntity;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 13:33
 * &#064;Version 1.0
 */
public class AccountEntityParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AccountEntityPageQuery extends PageParam {
        private String queryName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddAccountEntity implements IToTarget<AccountEntity> {
        private String name;
        private String entityType;
        private String registeredName;
        private String country;
        private String registrationNo;
        private String contactPerson;
        private String contactEmail;
        private String contactPhone;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateAccountEntity implements IToTarget<AccountEntity> {
        private Integer id;
        private String name;
        private String entityType;
        private String registeredName;
        private String country;
        private String registrationNo;
        private String contactPerson;
        private String contactEmail;
        private String contactPhone;
        private String comment;
    }

}
