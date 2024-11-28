package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.ecr.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonEcrService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/25 上午11:20
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsEcrRepo {

    private static final int MAX_RESULTS = 1000;

    public static List<Repository> describeRepositories(String regionId, EdsAwsConfigModel.Aws aws) {
        DescribeRepositoriesRequest request = new DescribeRepositoriesRequest();
        request.setMaxResults(MAX_RESULTS);
        List<Repository> repositories = Lists.newArrayList();
        while (true) {
            DescribeRepositoriesResult result = AmazonEcrService.buildAmazonECR(regionId, aws)
                    .describeRepositories(request);
            repositories.addAll(result.getRepositories());
            if (StringUtils.isNotBlank(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return repositories;
    }

    public static Repository describeRepository(String regionId, EdsAwsConfigModel.Aws aws, String repositoryName) {
        List<Repository> repositories = describeRepositories(regionId, aws, Lists.newArrayList(repositoryName));
        if (CollectionUtils.isEmpty(repositories)) return null;
        return repositories.getFirst();
    }

    /**
     * https://docs.aws.amazon.com/AmazonECR/latest/APIReference/API_DescribeRepositories.html
     *
     * @param regionId
     * @param aws
     * @param repositoryNames
     * @return
     */
    public static List<Repository> describeRepositories(String regionId, EdsAwsConfigModel.Aws aws,
                                                        List<String> repositoryNames) {
        DescribeRepositoriesRequest request = new DescribeRepositoriesRequest();
        request.setRepositoryNames(repositoryNames);
        List<Repository> repositories = Lists.newArrayList();
        while (true) {
            DescribeRepositoriesResult result = AmazonEcrService.buildAmazonECR(regionId, aws)
                    .describeRepositories(request);
            repositories.addAll(result.getRepositories());
            if (StringUtils.isNotBlank(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return repositories;
    }

    public static Repository createRepository(String regionId, EdsAwsConfigModel.Aws aws, String repositoryName) {
        CreateRepositoryRequest request = new CreateRepositoryRequest();
        request.setRepositoryName(repositoryName);
        CreateRepositoryResult result = AmazonEcrService.buildAmazonECR(regionId, aws)
                .createRepository(request);
        return result.getRepository();
    }

    public static void deleteRepository(String regionId, EdsAwsConfigModel.Aws aws, String registryId,
                                        @NonNull String repositoryName) {
        DeleteRepositoryRequest request = new DeleteRepositoryRequest()
                .withForce(true)
                .withRegistryId(registryId)
                .withRepositoryName(repositoryName);
        AmazonEcrService.buildAmazonECR(regionId, aws)
                .deleteRepository(request);
    }

}
