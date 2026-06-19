package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.cloudfront.model.DistributionSummary;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.aws.model.AwsCloudFrontDistribution;
import com.baiyi.cratos.eds.aws.repo.AwsCloudFrontRepo;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.service.TagService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/14 下午4:31
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_CLOUDFRONT_DISTRIBUTION)
public class EdsAwsCloudFrontDistributionAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Aws, AwsCloudFrontDistribution.Distribution> {

    private final TagService tagService;
    private final BusinessTagFacade businessTagFacade;

    public EdsAwsCloudFrontDistributionAssetProvider(EdsAssetProviderContext context, TagService tagService,
                                                     BusinessTagFacade businessTagFacade) {
        super(context);
        this.tagService = tagService;
        this.businessTagFacade = businessTagFacade;
    }

    @Override
    protected List<AwsCloudFrontDistribution.Distribution> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Aws> instance) throws EdsQueryEntitiesException {
        EdsConfigs.Aws aws = instance.getConfig();
        try {
            List<DistributionSummary> distributionSummaries = AwsCloudFrontRepo.listDistributions(aws);
            if (CollectionUtils.isEmpty(distributionSummaries)) {
                return Collections.emptyList();
            } else {
                return toDistributions(instance, distributionSummaries);
            }
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private List<AwsCloudFrontDistribution.Distribution> toDistributions(
            ExternalDataSourceInstance<EdsConfigs.Aws> instance, List<DistributionSummary> distributionSummaries) {
        return distributionSummaries.stream()
                .map(e -> AwsCloudFrontDistribution.Distribution.builder()
                        .distribution(AwsCloudFrontDistribution.DistributionDetail.from(e))
                        .aliases(e.getAliases() != null ? e.getAliases()
                                .getItems() : null)
                        .config(AwsCloudFrontDistribution.DistributionConfigDetail.from(
                                AwsCloudFrontRepo.getDistributionConfig(
                                        instance.getConfig()
                                                .getRegionId(), instance.getConfig(), e.getId()
                                )))
                        .build())
                .toList();
    }

    @Override
    protected EdsAsset toAsset(ExternalDataSourceInstance<EdsConfigs.Aws> instance,
                               AwsCloudFrontDistribution.Distribution entity) {
        // https://docs.aws.amazon.com/cloudfront/latest/APIReference/API_ListDistributions.html
        return createAssetBuilder(instance, entity).assetIdOf(entity.getDistribution()
                                                                      .getId())
                .nameOf(entity.getDistribution()
                                .getDomainName())
                .assetKeyOf(entity.getDistribution()
                                    .getArn())
                .descriptionOf(entity.getDistribution()
                                       .getComment())
                .build();
    }

    @Override
    protected void processAssetTags(EdsAsset asset, ExternalDataSourceInstance<EdsConfigs.Aws> instance,
                                    AwsCloudFrontDistribution.Distribution entity, List<EdsAssetIndex> indices) {
        if (!CollectionUtils.isEmpty(entity.getAliases()) && entity.getAliases()
                .size() == 1) {
            BusinessTagParam.SaveBusinessTag hostAliasBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                    .tagId(tagService.getByTagKey(SysTagKeys.HOST_ALIAS.getKey())
                                   .getId())
                    .businessType(BusinessTypeEnum.EDS_ASSET.name())
                    .businessId(asset.getId())
                    .tagValue(entity.getAliases()
                                      .getFirst())
                    .build();
            businessTagFacade.saveBusinessTag(hostAliasBusinessTag);
        }
        if (!CollectionUtils.isEmpty(entity.getConfig()
                                             .getOrigins())) {
            // 回源站点
            String host = entity.getConfig()
                    .getOrigins()
                    .getFirst()
                    .getDomainName();
            BusinessTagParam.SaveBusinessTag hostBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                    .tagId(tagService.getByTagKey(SysTagKeys.HOST.getKey())
                                   .getId())
                    .businessType(BusinessTypeEnum.EDS_ASSET.name())
                    .businessId(asset.getId())
                    .tagValue(host)
                    .build();
            businessTagFacade.saveBusinessTag(hostBusinessTag);
        }
    }

    @Override
    protected boolean isAssetUnchanged(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}