package com.baiyi.cratos.domain.view.acme;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/30 10:55
 * &#064;Version 1.0
 */
public class AcmeCertificateVO {

    public interface HasAcmeCertificateDeployments {
        Integer getCertificateId();

        void setCertificateDeployments(List<Deployment> certificateDeployments);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Certificate extends BaseVO implements HasAcmeCertificateDeployments, Serializable {
        @Serial
        private static final long serialVersionUID = -4801903387580004588L;
        private Integer id;
        private Integer accountId;
        private Integer domainId;
        private Integer orderId;
        private String domains;
        @JsonFormat(pattern = Global.ISO8601)
        private Date notBefore;
        @JsonFormat(pattern = Global.ISO8601)
        private Date notAfter;
        private String serialNumber;
        private String issuer;
        private Boolean valid;
        private String certificate;
        private String certificateChain;
        private String privateKey;

        private List<Deployment> certificateDeployments;

        @Override
        public Integer getCertificateId() {
            return id;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.ACME_CERTIFICATE_DEPLOYMENT)
    public static class Deployment extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = 1648264585132802218L;
        private Integer id;
        private Integer edsInstanceId;
        private String edsInstanceName;
        private String edsCertificateId;
        private String edsCertificateName;
        private Integer certificateId;
        private String domain;
        private String domains;
        @JsonFormat(pattern = Global.ISO8601)
        private Date notBefore;
        @JsonFormat(pattern = Global.ISO8601)
        private Date notAfter;
        private Boolean valid;
        private String deploymentDetails;
    }

}
