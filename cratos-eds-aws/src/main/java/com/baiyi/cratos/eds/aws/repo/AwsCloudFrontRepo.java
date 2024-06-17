package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.cloudfront.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonCloudFrontService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/14 下午3:26
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsCloudFrontRepo {

    public static List<DistributionSummary> listDistributions(EdsAwsConfigModel.Aws aws) {
        return listDistributions(aws.getRegionId(), aws);
    }

    public static List<DistributionSummary> listDistributions(String regionId, EdsAwsConfigModel.Aws aws) {
        ListDistributionsRequest request = new ListDistributionsRequest();
        List<DistributionSummary> distributionSummaryList = Lists.newArrayList();
        while (true) {
            ListDistributionsResult result = AmazonCloudFrontService.buildAmazonCloudFront(regionId, aws)
                    .listDistributions(request);
            List<DistributionSummary> distributionSummaries = Optional.of(result)
                    .map(ListDistributionsResult::getDistributionList)
                    .map(DistributionList::getItems)
                    .orElse(Collections.emptyList());
            distributionSummaryList.addAll(distributionSummaries);
            if (StringUtils.hasText(result.getDistributionList()
                    .getNextMarker())) {
                request.setMarker(result.getDistributionList()
                        .getNextMarker());
            } else {
                break;
            }
        }
        return distributionSummaryList;
    }

    public static List<ConflictingAlias> listConflictingAliases(EdsAwsConfigModel.Aws aws, String distributionId) {
        return listConflictingAliases(aws.getRegionId(), aws, distributionId);
    }

    public static List<ConflictingAlias> listConflictingAliases(String regionId, EdsAwsConfigModel.Aws aws,
                                                                String distributionId) {
        ListConflictingAliasesRequest request = new ListConflictingAliasesRequest()
                .withDistributionId(distributionId);
        List<ConflictingAlias> conflictingAliasList = Lists.newArrayList();
        while (true) {
            ListConflictingAliasesResult result = AmazonCloudFrontService.buildAmazonCloudFront(regionId, aws)
                    .listConflictingAliases(request);
            List<ConflictingAlias> conflictingAliases = Optional.of(result)
                    .map(ListConflictingAliasesResult::getConflictingAliasesList)
                    .map(ConflictingAliasesList::getItems)
                    .orElse(Collections.emptyList());
            conflictingAliasList.addAll(conflictingAliases);
            if (StringUtils.hasText(result.getConflictingAliasesList()
                    .getNextMarker())) {
                request.setMarker(result.getConflictingAliasesList()
                        .getNextMarker());
            } else {
                break;
            }
        }
        return conflictingAliasList;
    }

}
