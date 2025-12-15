package com.baiyi.cratos.eds.aws.provider.iam;

import com.amazonaws.services.identitymanagement.model.Policy;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamPolicyRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/4 18:10
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_IAM_POLICY)
public class EdsAwsIamPolicyProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Aws, Policy> {

    private final AwsIamPolicyRepo awsIamPolicyRepo;

    public EdsAwsIamPolicyProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                   CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                   EdsAssetIndexFacade edsAssetIndexFacade,
                                   UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                   EdsInstanceProviderHolderBuilder holderBuilder, AwsIamPolicyRepo awsIamPolicyRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.awsIamPolicyRepo = awsIamPolicyRepo;
    }

    @Override
    protected List<Policy> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Aws> instance) throws EdsQueryEntitiesException {
        EdsConfigs.Aws aws = instance.getConfig();
        try {
            return awsIamPolicyRepo.listPolicies(aws);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aws> instance, Policy entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getPolicyId())
                .nameOf(entity.getPolicyName())
                .assetKeyOf(entity.getArn())
                .descriptionOf(entity.getDescription())
                .createdTimeOf(entity.getCreateDate())
                .build();
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}