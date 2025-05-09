package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.sdk.service.kms20160120.AsyncClient;
import com.aliyun.sdk.service.kms20160120.models.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunKmsClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
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

    public static List<ListKeysResponseBody.Key> listKeys(String endpoint, EdsAliyunConfigModel.Aliyun aliyun) {
        List<ListKeysResponseBody.Key> result = Lists.newArrayList();
        int pageNumber = 1;
        try (AsyncClient client = AliyunKmsClient.buildKmsClient(endpoint, aliyun)) {
            while (true) {
                ListKeysRequest listKeysRequest = ListKeysRequest.builder()
                        .pageNumber(pageNumber)
                        .pageSize(100)
                        .build();
                ListKeysResponse resp = client.listKeys(listKeysRequest)
                        .get();
                List<ListKeysResponseBody.Key> keys = Optional.ofNullable(resp)
                        .map(ListKeysResponse::getBody)
                        .map(ListKeysResponseBody::getKeys)
                        .map(ListKeysResponseBody.Keys::getKey)
                        .orElse(List.of());
                result.addAll(keys);
                if (result.size() >= Objects.requireNonNull(resp)
                        .getBody()
                        .getTotalCount()) {
                    break;
                }
                pageNumber++;
            }
        } catch (Exception e) {
            log.error("Failed to list keys from Aliyun KMS: {}", e.getMessage(), e);
        }
        return result;
    }

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
        try (AsyncClient client = AliyunKmsClient.buildKmsClient(endpoint, aliyun)) {
            while (true) {
                ListSecretsRequest request = ListSecretsRequest.builder()
                        .pageNumber(pageNumber)
                        .pageSize(100)
                        .build();
                ListSecretsResponse resp = client.listSecrets(request)
                        .get();
                List<ListSecretsResponseBody.Secret> secrets = Optional.ofNullable(resp)
                        .map(ListSecretsResponse::getBody)
                        .map(ListSecretsResponseBody::getSecretList)
                        .map(ListSecretsResponseBody.SecretList::getSecret)
                        .orElse(List.of());
                result.addAll(secrets);
                if (result.size() >= Objects.requireNonNull(resp)
                        .getBody()
                        .getTotalCount()) {
                    break;
                }
                pageNumber++;
            }
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

}
