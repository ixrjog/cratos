package com.baiyi.cratos.facade.inspection.impl;

import com.aliyun.oss.model.Bucket;
import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.aliyun.model.AliyunOss;
import com.baiyi.cratos.eds.aliyun.repo.AliyunOssRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.inspection.base.BaseEdsInspectionTask;
import com.baiyi.cratos.facade.inspection.context.InspectionTaskContext;
import com.baiyi.cratos.facade.inspection.model.AliyunOssBucketModel;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.ALIYUN_OSS_BUCKET_POLICY_INSPECTION_NOTIFICATION;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/6 13:58
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class AliyunOssBucketPolicyInspectionTask extends BaseEdsInspectionTask<EdsConfigs.Aliyun> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Pattern RESOURCE_PREFIX_PATTERN = Pattern.compile("acs:oss:\\*:\\d+:");
    private static final String POLICIES_FIELD = "policies";

    public AliyunOssBucketPolicyInspectionTask(InspectionTaskContext context,
                                               EdsProviderHolderFactory edsProviderHolderFactory,
                                               EdsInstanceService edsInstanceService,
                                               ConfigCredTemplate configCredTemplate,
                                               CredentialService credentialService, EdsAssetService edsAssetService) {
        super(
                context, edsProviderHolderFactory, edsInstanceService, configCredTemplate, credentialService,
                edsAssetService
        );
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                ALIYUN_OSS_BUCKET_POLICY_INSPECTION_NOTIFICATION);
        List<AliyunOssBucketModel.Policy> policies = queryAliyunOssBucketPolicies();
        return BeetlUtil.renderTemplate(
                notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                        .put(POLICIES_FIELD, policies)
                        .build()
        );
    }

    private List<AliyunOssBucketModel.Policy> queryAliyunOssBucketPolicies() {
        return edsInstanceService.queryValidEdsInstanceByType(EdsInstanceTypeEnum.ALIYUN.name())
                .stream()
                .flatMap(this::queryInstancePolicies)
                .collect(Collectors.toList());
    }

    private Stream<AliyunOssBucketModel.Policy> queryInstancePolicies(EdsInstance instance) {
        EdsConfigs.Aliyun aliyun = getConfig(instance.getConfigId());
        String endpoint = Optional.ofNullable(aliyun.getOss())
                .map(EdsAliyunConfigModel.OSS::getEndpoints)
                .filter(list -> !list.isEmpty())
                .map(List::getFirst)
                .orElse(null);
        if (endpoint == null) {
            return Stream.empty();
        }
        Map<String, EdsAsset> ramUserMap = edsAssetService.queryInstanceAssets(
                        instance.getId(), EdsAssetTypeEnum.ALIYUN_RAM_USER.name())
                .stream()
                .collect(Collectors.toMap(EdsAsset::getAssetId, Function.identity(), (a, b) -> a));

        return AliyunOssRepo.listBuckets(endpoint, aliyun)
                .stream()
                .flatMap(bucket -> processBucket(bucket, aliyun, ramUserMap, instance.getInstanceName()));
    }

    private Stream<AliyunOssBucketModel.Policy> processBucket(Bucket bucket, EdsConfigs.Aliyun aliyun,
                                                              Map<String, EdsAsset> ramUserMap, String instanceName) {
        try {
            String policyJson = AliyunOssRepo.getBucketPolicy(bucket.getExtranetEndpoint(), aliyun, bucket.getName());
            if (policyJson == null) {
                return Stream.empty();
            }
            AliyunOss.BucketPolicy bucketPolicy = OBJECT_MAPPER.readValue(policyJson, AliyunOss.BucketPolicy.class);
            return Optional.ofNullable(bucketPolicy.getStatement())
                    .orElse(List.of())
                    .stream()
                    .flatMap(statement -> Optional.ofNullable(statement.getPrincipal())
                            .orElse(List.of())
                            .stream()
                            .map(ramUserMap::get)
                            .filter(Objects::nonNull)
                            .filter(asset -> !asset.getAssetKey()
                                    .startsWith("ak-"))
                            .map(asset -> AliyunOssBucketModel.Policy.builder()
                                    .instanceName(instanceName)
                                    .ramUser(asset.getAssetKey())
                                    .ramName(asset.getName())
                                    .endpoint(bucket.getExtranetEndpoint())
                                    .bucketName(bucket.getName())
                                    .effect(statement.getEffect())
                                    .resources(convertResources(statement))
                                    .action(String.join(",", statement.getAction()))
                                    .build()));
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse bucket policy: bucket={}, error={}", bucket.getName(), e.getMessage());
            return Stream.empty();
        }
    }

    private static String convertResources(AliyunOss.Statement statement) {
        return statement.getResource()
                .stream()
                .map(s -> RESOURCE_PREFIX_PATTERN.matcher(s)
                        .replaceAll(""))
                .collect(Collectors.joining(","));
    }

}
