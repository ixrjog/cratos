package com.baiyi.cratos.domain.param.certificate;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.IImportFromAsset;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:20
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CertificateParam {

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.CERTIFICATE)
    public static class AddCertificate implements IToTarget<Certificate>, IImportFromAsset {

        private String certificateId;

        private String name;

        @Schema(description = "域名")
        private String domainName;

        @Schema(description = "证书类型")
        private String certificateType;

        @Schema(description = "有效")
        private Boolean valid;

        @Schema(description = "算法")
        private String keyAlgorithm;

        @Schema(description = "不早于")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date notBefore;

        @Schema(description = "不晚于")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date notAfter;

        private String comment;

        @Schema(description = "Import from assetId")
        private Integer fromAssetId;

    }

    @Data
    @Schema
    public static class UpdateCertificate implements IToTarget<Certificate> {

        private Integer id;

        private String certificateId;

        private String name;

        @Schema(description = "域名")
        private String domainName;

        @Schema(description = "证书类型")
        private String certificateType;

        @Schema(description = "有效")
        private Boolean valid;

        @Schema(description = "算法")
        private String keyAlgorithm;

        @Schema(description = "不早于")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date notBefore;

        @Schema(description = "不晚于")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date notAfter;

        private String comment;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class CertificatePageQuery extends PageParam {

        @Schema(description = "查询名称")
        private String queryName;

    }

}