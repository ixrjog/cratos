package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.cloudfront.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonCloudFrontService;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

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
        String nextMarker = null;
        do {
            if (nextMarker != null) {
                request.setMarker(nextMarker);
            }
            ListDistributionsResult result = AmazonCloudFrontService.buildAmazonCloudFront(regionId, aws)
                    .listDistributions(request);
            DistributionList distributionList = result.getDistributionList();
            if (distributionList != null && distributionList.getItems() != null) {
                distributionSummaryList.addAll(distributionList.getItems());
            }
            nextMarker = (distributionList != null && StringUtils.hasText(
                    distributionList.getNextMarker())) ? distributionList.getNextMarker() : null;
        } while (nextMarker != null);
        return distributionSummaryList;
    }

    public static List<ConflictingAlias> listConflictingAliases(EdsAwsConfigModel.Aws aws, String distributionId) {
        return listConflictingAliases(aws.getRegionId(), aws, distributionId);
    }

    public static List<ConflictingAlias> listConflictingAliases(String regionId, EdsAwsConfigModel.Aws aws,
                                                                String distributionId) {
        ListConflictingAliasesRequest request = new ListConflictingAliasesRequest().withDistributionId(distributionId);
        List<ConflictingAlias> conflictingAliasList = Lists.newArrayList();
        String nextMarker = null;
        do {
            if (nextMarker != null) {
                request.setMarker(nextMarker);
            }
            ListConflictingAliasesResult result = AmazonCloudFrontService.buildAmazonCloudFront(regionId, aws)
                    .listConflictingAliases(request);
            ConflictingAliasesList aliasesList = result.getConflictingAliasesList();
            if (aliasesList != null && aliasesList.getItems() != null) {
                conflictingAliasList.addAll(aliasesList.getItems());
            }
            nextMarker = (aliasesList != null && StringUtils.hasText(
                    aliasesList.getNextMarker())) ? aliasesList.getNextMarker() : null;
        } while (nextMarker != null);
        return conflictingAliasList;
    }

}
