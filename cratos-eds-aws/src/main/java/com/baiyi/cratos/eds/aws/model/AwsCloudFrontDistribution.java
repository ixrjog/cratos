package com.baiyi.cratos.eds.aws.model;

import com.amazonaws.services.cloudfront.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/14 下午5:16
 * &#064;Version 1.0
 * <p>
 * 说明:这里全部使用普通 POJO(标准 getter/setter + 普通 List),而不是直接持有 AWS SDK 的模型对象。
 * 原因:AWS SDK v1 的模型用 {@code SdkInternalList}、懒加载 auto-construct、以及 getter/setter 类型不一致
 * (如 {@code List getItems()} 对 {@code setItems(Collection)}),导致 SnakeYAML 把这些属性当成只读属性
 * 在 dump 时直接丢弃(默认 allowReadOnlyProperties=false),从而丢失 origins.items 等信息。
 * 改为普通 POJO 后可稳定 round-trip。
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsCloudFrontDistribution {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Distribution {
        private DistributionDetail distribution;
        private List<String> aliases;
        private DistributionConfigDetail config;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DistributionDetail {
        private String id;
        private String arn;
        private String status;
        private String domainName;
        private String comment;
        private Boolean enabled;
        private String httpVersion;
        private Boolean ipV6Enabled;
        private List<String> aliases;

        public static DistributionDetail from(DistributionSummary s) {
            if (s == null) {
                return null;
            }
            return DistributionDetail.builder()
                    .id(s.getId())
                    .arn(s.getARN())
                    .status(s.getStatus())
                    .domainName(s.getDomainName())
                    .comment(s.getComment())
                    .enabled(s.getEnabled())
                    .httpVersion(s.getHttpVersion())
                    .ipV6Enabled(s.getIsIPV6Enabled())
                    .aliases(s.getAliases() != null ? s.getAliases()
                            .getItems() : null)
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DistributionConfigDetail {
        private String callerReference;
        private String comment;
        private Boolean enabled;
        private String defaultRootObject;
        private String priceClass;
        private String httpVersion;
        private Boolean ipV6Enabled;
        private String webACLId;
        private List<String> aliases;
        private List<OriginDetail> origins;
        private CacheBehaviorDetail defaultCacheBehavior;
        private List<CacheBehaviorDetail> cacheBehaviors;
        private ViewerCertificateDetail viewerCertificate;

        public static DistributionConfigDetail from(DistributionConfig c) {
            if (c == null) {
                return null;
            }
            return DistributionConfigDetail.builder()
                    .callerReference(c.getCallerReference())
                    .comment(c.getComment())
                    .enabled(c.getEnabled())
                    .defaultRootObject(c.getDefaultRootObject())
                    .priceClass(c.getPriceClass())
                    .httpVersion(c.getHttpVersion())
                    .ipV6Enabled(c.getIsIPV6Enabled())
                    .webACLId(c.getWebACLId())
                    .aliases(c.getAliases() != null ? c.getAliases()
                            .getItems() : null)
                    .origins(c.getOrigins() != null && c.getOrigins()
                            .getItems() != null ? c.getOrigins()
                            .getItems()
                            .stream()
                            .map(OriginDetail::from)
                            .toList() : null)
                    .defaultCacheBehavior(CacheBehaviorDetail.from(c.getDefaultCacheBehavior()))
                    .cacheBehaviors(c.getCacheBehaviors() != null && c.getCacheBehaviors()
                            .getItems() != null ? c.getCacheBehaviors()
                            .getItems()
                            .stream()
                            .map(CacheBehaviorDetail::from)
                            .toList() : null)
                    .viewerCertificate(ViewerCertificateDetail.from(c.getViewerCertificate()))
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OriginDetail {
        private String id;
        private String domainName;
        private String originPath;
        private List<CustomHeaderDetail> customHeaders;
        private S3OriginConfigDetail s3OriginConfig;
        private CustomOriginConfigDetail customOriginConfig;
        private Integer connectionAttempts;
        private Integer connectionTimeout;

        public static OriginDetail from(Origin o) {
            if (o == null) {
                return null;
            }
            return OriginDetail.builder()
                    .id(o.getId())
                    .domainName(o.getDomainName())
                    .originPath(o.getOriginPath())
                    .customHeaders(o.getCustomHeaders() != null && o.getCustomHeaders()
                            .getItems() != null ? o.getCustomHeaders()
                            .getItems()
                            .stream()
                            .map(CustomHeaderDetail::from)
                            .toList() : null)
                    .s3OriginConfig(S3OriginConfigDetail.from(o.getS3OriginConfig()))
                    .customOriginConfig(CustomOriginConfigDetail.from(o.getCustomOriginConfig()))
                    .connectionAttempts(o.getConnectionAttempts())
                    .connectionTimeout(o.getConnectionTimeout())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomHeaderDetail {
        private String headerName;
        private String headerValue;

        public static CustomHeaderDetail from(OriginCustomHeader h) {
            if (h == null) {
                return null;
            }
            return CustomHeaderDetail.builder()
                    .headerName(h.getHeaderName())
                    .headerValue(h.getHeaderValue())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class S3OriginConfigDetail {
        private String originAccessIdentity;

        public static S3OriginConfigDetail from(S3OriginConfig c) {
            if (c == null) {
                return null;
            }
            return S3OriginConfigDetail.builder()
                    .originAccessIdentity(c.getOriginAccessIdentity())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomOriginConfigDetail {
        private Integer httpPort;
        private Integer httpsPort;
        private String originProtocolPolicy;
        private List<String> originSslProtocols;
        private Integer originReadTimeout;
        private Integer originKeepaliveTimeout;

        public static CustomOriginConfigDetail from(CustomOriginConfig c) {
            if (c == null) {
                return null;
            }
            return CustomOriginConfigDetail.builder()
                    .httpPort(c.getHTTPPort())
                    .httpsPort(c.getHTTPSPort())
                    .originProtocolPolicy(c.getOriginProtocolPolicy())
                    .originSslProtocols(c.getOriginSslProtocols() != null ? c.getOriginSslProtocols()
                            .getItems() : null)
                    .originReadTimeout(c.getOriginReadTimeout())
                    .originKeepaliveTimeout(c.getOriginKeepaliveTimeout())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CacheBehaviorDetail {
        private String pathPattern;
        private String targetOriginId;
        private String viewerProtocolPolicy;
        private List<String> allowedMethods;
        private List<String> cachedMethods;
        private Boolean compress;
        private Long minTTL;
        private Long defaultTTL;
        private Long maxTTL;

        public static CacheBehaviorDetail from(DefaultCacheBehavior b) {
            if (b == null) {
                return null;
            }
            return CacheBehaviorDetail.builder()
                    .targetOriginId(b.getTargetOriginId())
                    .viewerProtocolPolicy(b.getViewerProtocolPolicy())
                    .allowedMethods(allowedMethodsOf(b.getAllowedMethods()))
                    .cachedMethods(cachedMethodsOf(b.getAllowedMethods()))
                    .compress(b.getCompress())
                    .minTTL(b.getMinTTL())
                    .defaultTTL(b.getDefaultTTL())
                    .maxTTL(b.getMaxTTL())
                    .build();
        }

        public static CacheBehaviorDetail from(CacheBehavior b) {
            if (b == null) {
                return null;
            }
            return CacheBehaviorDetail.builder()
                    .pathPattern(b.getPathPattern())
                    .targetOriginId(b.getTargetOriginId())
                    .viewerProtocolPolicy(b.getViewerProtocolPolicy())
                    .allowedMethods(allowedMethodsOf(b.getAllowedMethods()))
                    .cachedMethods(cachedMethodsOf(b.getAllowedMethods()))
                    .compress(b.getCompress())
                    .minTTL(b.getMinTTL())
                    .defaultTTL(b.getDefaultTTL())
                    .maxTTL(b.getMaxTTL())
                    .build();
        }

        private static List<String> allowedMethodsOf(AllowedMethods m) {
            return m != null ? m.getItems() : null;
        }

        private static List<String> cachedMethodsOf(AllowedMethods m) {
            return m != null && m.getCachedMethods() != null ? m.getCachedMethods()
                    .getItems() : null;
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewerCertificateDetail {
        private Boolean cloudFrontDefaultCertificate;
        private String acmCertificateArn;
        private String sslSupportMethod;
        private String minimumProtocolVersion;
        private String certificate;
        private String certificateSource;

        public static ViewerCertificateDetail from(ViewerCertificate v) {
            if (v == null) {
                return null;
            }
            return ViewerCertificateDetail.builder()
                    .cloudFrontDefaultCertificate(v.getCloudFrontDefaultCertificate())
                    .acmCertificateArn(v.getACMCertificateArn())
                    .sslSupportMethod(v.getSSLSupportMethod())
                    .minimumProtocolVersion(v.getMinimumProtocolVersion())
                    .certificate(v.getCertificate())
                    .certificateSource(v.getCertificateSource())
                    .build();
        }
    }

}
