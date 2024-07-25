package com.baiyi.cratos.domain.param.domain;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.param.IImportFromAsset;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:01
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class DomainParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class DomainPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;
        public DomainPageQueryParam toParam() {
            return DomainPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class DomainPageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.DOMAIN)
    public static class AddDomain implements IToTarget<Domain>, IImportFromAsset {
        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 有效
         */
        private Boolean valid;
        @Schema(description = "注册时间")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date registrationTime;
        @Schema(description = "到期")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiry;
        private String domainType;
        private String comment;
        @Schema(description = "Import from assetId")
        private Integer fromAssetId;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.DOMAIN)
    public static class UpdateDomain implements IToTarget<Domain>, IImportFromAsset {
        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 有效
         */
        private Boolean valid;
        @Schema(description = "注册时间")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date registrationTime;
        @Schema(description = "到期")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiry;
        private String domainType;
        private String comment;
        @Schema(description = "Import from assetId")
        private Integer fromAssetId;
    }

}
