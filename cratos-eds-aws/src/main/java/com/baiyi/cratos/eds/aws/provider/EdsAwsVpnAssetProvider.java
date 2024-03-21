package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.VpnConnection;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.AwsVpnRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/3/6 10:26
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.AWS, assetType = EdsAssetTypeEnum.AWS_STS_VPN)
public class EdsAwsVpnAssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, VpnConnection> {

    @Override
    protected List<VpnConnection> listEntities(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        try {
            EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
            Set<String> reggionIdSet = Sets.newHashSet(aws.getRegionId());
            reggionIdSet.addAll(Optional.of(aws)
                    .map(EdsAwsConfigModel.Aws::getRegionIds)
                    .orElse(null));
            List<VpnConnection> entities = Lists.newArrayList();
            reggionIdSet.forEach(regionId -> entities.addAll(AwsVpnRepo.describeVpnConnections(regionId, aws)));
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, VpnConnection entity) {
        // https://docs.aws.amazon.com/acm/latest/APIReference/API_ListCertificates.html
        Optional<Tag> optionalTag = entity.getTags()
                .stream()
                .filter(e -> "Name".equals(e.getKey()))
                .findAny();
        String name = optionalTag.isPresent() ? optionalTag.get()
                .getValue() : entity.getVpnConnectionId();
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getVpnConnectionId())
                .nameOf(name)
                .kindOf(entity.getType())
                .statusOf(entity.getState())
                .build();
    }

}
