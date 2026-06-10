package com.baiyi.cratos.eds.aliyun.util;

import com.aliyun.oss.model.Bucket;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.aliyun.model.AliyunOss;
import com.baiyi.cratos.eds.aliyun.repo.AliyunOSSRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 10:02
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunOssBucketSecUtils {

    private final EdsInstanceService edsInstanceService;
    private final EdsAssetService edsAssetService;
    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Pattern RESOURCE_PREFIX_PATTERN = Pattern.compile("acs:oss:\\*:\\d+:");
    private static final String POLICIES_FIELD = "policies";

    public void run() {
        List<AliyunOss.Policy> policies = queryAliyunOssBucketPolicies();
    }

    private List<AliyunOss.Policy> queryAliyunOssBucketPolicies() {
        return edsInstanceService.queryValidEdsInstanceByType(EdsInstanceTypeEnum.ALIYUN.name())
                .stream()
                .flatMap(this::queryInstancePolicies)
                .collect(Collectors.toList());
    }

    private EdsConfigs.Aliyun getConfig(EdsInstance instance) {
        EdsInstanceProviderHolder<EdsConfigs.Aliyun, ?> providerHolder = (EdsInstanceProviderHolder<EdsConfigs.Aliyun, ?>) edsProviderHolderFactory.createHolder(
                instance.getId(), EdsAssetTypeEnum.ALIYUN_OSS_BUCKET.name());
        return providerHolder.getInstance()
                .getConfig();
    }

    private Stream<AliyunOss.Policy> queryInstancePolicies(EdsInstance instance) {
        EdsConfigs.Aliyun aliyun = getConfig(instance);
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

        return AliyunOSSRepo.listBuckets(endpoint, aliyun)
                .stream()
                .flatMap(bucket -> processBucket(bucket, aliyun, ramUserMap, instance.getInstanceName()));
    }

    private Stream<AliyunOss.Policy> processBucket(Bucket bucket, EdsConfigs.Aliyun aliyun,
                                                   Map<String, EdsAsset> ramUserMap, String instanceName) {
        try {
            String policyJson = AliyunOSSRepo.getBucketPolicy(bucket.getExtranetEndpoint(), aliyun, bucket.getName());
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
                            .map(asset -> AliyunOss.Policy.builder()
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
