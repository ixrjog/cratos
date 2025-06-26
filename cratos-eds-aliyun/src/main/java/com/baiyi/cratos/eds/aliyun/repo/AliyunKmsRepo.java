package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.sdk.service.kms20160120.AsyncClient;
import com.aliyun.sdk.service.kms20160120.models.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunKmsClient;
import com.baiyi.cratos.eds.aliyun.util.AliyunTagUtils;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/8 16:19
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunKmsRepo {

    public static List<ListKmsInstancesResponseBody.KmsInstance> listInstances(String endpoint,
                                                                               EdsAliyunConfigModel.Aliyun aliyun) {
        try (AsyncClient client = AliyunKmsClient.buildKmsClient(endpoint, aliyun)) {
            ListKmsInstancesRequest listKmsInstancesRequest = ListKmsInstancesRequest.builder()
                    .build();
            CompletableFuture<ListKmsInstancesResponse> response = client.listKmsInstances(listKmsInstancesRequest);
            ListKmsInstancesResponse resp = response.get();
            return Optional.of(resp)
                    .map(ListKmsInstancesResponse::getBody)
                    .map(ListKmsInstancesResponseBody::getKmsInstances)
                    .map(ListKmsInstancesResponseBody.KmsInstances::getKmsInstance)
                    .orElse(List.of());
        } catch (Exception e) {
            return List.of();
        }
    }

    // Key
    public static List<ListKeysResponseBody.Key> listKeys(String endpoint, EdsAliyunConfigModel.Aliyun aliyun) {
        List<ListKeysResponseBody.Key> result = Lists.newArrayList();
        int pageNumber = 1;
        int pageSize = 100;
        try (AsyncClient client = AliyunKmsClient.buildKmsClient(endpoint, aliyun)) {
            int totalCount;
            do {
                ListKeysRequest listKeysRequest = ListKeysRequest.builder()
                        .pageNumber(pageNumber)
                        .pageSize(pageSize)
                        .build();
                ListKeysResponse resp = client.listKeys(listKeysRequest)
                        .get();
                List<ListKeysResponseBody.Key> keys = Optional.ofNullable(resp)
                        .map(ListKeysResponse::getBody)
                        .map(ListKeysResponseBody::getKeys)
                        .map(ListKeysResponseBody.Keys::getKey)
                        .orElse(List.of());
                result.addAll(keys);
                totalCount = Optional.ofNullable(resp)
                        .map(ListKeysResponse::getBody)
                        .map(ListKeysResponseBody::getTotalCount)
                        .orElse(0);
                pageNumber++;
            } while (result.size() < totalCount);
        } catch (Exception e) {
            log.error("Failed to list keys from Aliyun KMS: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * https://help.aliyun.com/zh/kms/key-management-service/developer-reference/api-describekey?spm=a2c4g.11186623.help-menu-28933.d_5_0_0_7_5.6952229f1KWGLP&scm=20140722.H_28952._.OR_help-T_cn~zh-V_1
     *
     * @param endpoint
     * @param aliyun
     * @param keyId
     * @return
     */
    public static Optional<DescribeKeyResponseBody.KeyMetadata> describeKey(String endpoint,
                                                                            EdsAliyunConfigModel.Aliyun aliyun,
                                                                            String keyId) {
        try (AsyncClient client = AliyunKmsClient.buildKmsClient(endpoint, aliyun)) {
            DescribeKeyRequest request = DescribeKeyRequest.builder()
                    .keyId(keyId)
                    .build();
            DescribeKeyResponse resp = client.describeKey(request)
                    .get();
            return Optional.ofNullable(resp)
                    .map(DescribeKeyResponse::getBody)
                    .map(DescribeKeyResponseBody::getKeyMetadata);
        } catch (Exception e) {
            log.error("Failed to describe key from Aliyun KMS: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public static List<ListSecretsResponseBody.Secret> listSecrets(String endpoint,
                                                                   EdsAliyunConfigModel.Aliyun aliyun) {
        List<ListSecretsResponseBody.Secret> result = Lists.newArrayList();
        int pageNumber = 1;
        int pageSize = 100;
        try (AsyncClient client = AliyunKmsClient.buildKmsClient(endpoint, aliyun)) {
            int totalCount;
            do {
                ListSecretsRequest request = ListSecretsRequest.builder()
                        .pageNumber(pageNumber)
                        .pageSize(pageSize)
                        .build();
                ListSecretsResponse resp = client.listSecrets(request)
                        .get();
                List<ListSecretsResponseBody.Secret> secrets = Optional.ofNullable(resp)
                        .map(ListSecretsResponse::getBody)
                        .map(ListSecretsResponseBody::getSecretList)
                        .map(ListSecretsResponseBody.SecretList::getSecret)
                        .orElse(List.of());
                result.addAll(secrets);
                totalCount = Optional.ofNullable(resp)
                        .map(ListSecretsResponse::getBody)
                        .map(ListSecretsResponseBody::getTotalCount)
                        .orElse(0);
                pageNumber++;
            } while (result.size() < totalCount);
        } catch (Exception e) {
            log.error("Failed to list secrets from Aliyun KMS: {}", e.getMessage(), e);
        }
        return result;
    }

    public static Optional<DescribeSecretResponseBody> describeSecret(String endpoint,
                                                                      EdsAliyunConfigModel.Aliyun aliyun,
                                                                      String secretName) {
        try (AsyncClient client = AliyunKmsClient.buildKmsClient(endpoint, aliyun)) {
            DescribeSecretRequest request = DescribeSecretRequest.builder()
                    .secretName(secretName)
                    .fetchTags("true")
                    .build();
            DescribeSecretResponse resp = client.describeSecret(request)
                    .get();
            return Optional.ofNullable(resp)
                    .map(DescribeSecretResponse::getBody);
        } catch (Exception e) {
            log.error("Failed to describe secret from Aliyun KMS: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }


    public static List<ListSecretVersionIdsResponseBody.VersionId> listSecretVersionIds(String endpoint,
                                                                                        EdsAliyunConfigModel.Aliyun aliyun,
                                                                                        String secretName) {
        List<ListSecretVersionIdsResponseBody.VersionId> result = Lists.newArrayList();
        int pageNumber = 1;
        int pageSize = 100;
        try (AsyncClient client = AliyunKmsClient.buildKmsClient(endpoint, aliyun)) {
            int totalCount;
            do {
                ListSecretVersionIdsRequest request = ListSecretVersionIdsRequest.builder()
                        .secretName(secretName)
                        .pageNumber(pageNumber)
                        .pageSize(pageSize)
                        .build();
                ListSecretVersionIdsResponse resp = client.listSecretVersionIds(request)
                        .get();
                List<ListSecretVersionIdsResponseBody.VersionId> versionIds = Optional.ofNullable(resp)
                        .map(ListSecretVersionIdsResponse::getBody)
                        .map(ListSecretVersionIdsResponseBody::getVersionIds)
                        .map(ListSecretVersionIdsResponseBody.VersionIds::getVersionId)
                        .orElse(List.of());
                result.addAll(versionIds);
                totalCount = Optional.ofNullable(resp)
                        .map(ListSecretVersionIdsResponse::getBody)
                        .map(ListSecretVersionIdsResponseBody::getTotalCount)
                        .orElse(0);
                pageNumber++;
            } while (result.size() < totalCount);
        } catch (Exception e) {
            log.error("Failed to list secret version ids from Aliyun KMS: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * https://help.aliyun.com/zh/kms/key-management-service/developer-reference/api-createsecret?spm=a2c4g.11186623.help-menu-28933.d_5_0_0_9_0.75c817c3mDAvZ5&scm=20140722.H_154490._.OR_help-T_cn~zh-V_1
     *
     * @param endpoint
     * @param aliyun
     * @param kmsInstanceId
     * @param secretName
     * @param versionId
     * @param encryptionKeyId
     * @param secretData
     * @param tags
     * @param description
     * @return
     */
    public static Optional<CreateSecretResponseBody> createSecret(String endpoint, EdsAliyunConfigModel.Aliyun aliyun,
                                                                  String kmsInstanceId, String secretName,
                                                                  String versionId, String encryptionKeyId,
                                                                  String secretData, Map<String, String> tags,
                                                                  String description) {
        try (AsyncClient client = AliyunKmsClient.buildKmsClient(endpoint, aliyun)) {
            CreateSecretRequest request = CreateSecretRequest.builder()
                    .DKMSInstanceId(kmsInstanceId)
                    .secretName(secretName)
                    .versionId(versionId)
                    .encryptionKeyId(encryptionKeyId)
                    .secretData(secretData)
                    .secretDataType("text")
                    .description(description)
                    .tags(CollectionUtils.isEmpty(tags) ? null : AliyunTagUtils.convertMapToTagsJsonStream(tags))
                    // SecretType Generic
                    .build();
            CreateSecretResponse resp = client.createSecret(request)
                    .get();
            return Optional.ofNullable(resp)
                    .map(CreateSecretResponse::getBody);
        } catch (Exception e) {
            log.error("Failed to create secret from Aliyun KMS: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

}
