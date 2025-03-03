package com.baiyi.cratos.eds.aws.provider.iam;

import com.amazonaws.services.identitymanagement.model.AttachedPolicy;
import com.amazonaws.services.identitymanagement.model.User;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamPolicyRepo;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamUserRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
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
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.AWS_IAM_POLICIES;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCOUNT_USERNAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/17 16:14
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_IAM_USER)
public class EdsAwsIamUserProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, User> {

    private final AwsIamUserRepo awsIamUserRepo;
    private final AwsIamPolicyRepo awsIamPolicyRepo;

    public EdsAwsIamUserProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                 CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                 EdsAssetIndexFacade edsAssetIndexFacade,
                                 UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                 EdsInstanceProviderHolderBuilder holderBuilder, AwsIamUserRepo awsIamUserRepo,
                                 AwsIamPolicyRepo awsIamPolicyRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.awsIamUserRepo = awsIamUserRepo;
        this.awsIamPolicyRepo = awsIamPolicyRepo;
    }

    @Override
    protected List<User> listEntities(
            ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
        try {
            return awsIamUserRepo.listUsers(aws);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, User entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getUserId())
                .nameOf(entity.getUserName())
                .assetKeyOf(entity.getArn())
                .createdTimeOf(entity.getCreateDate())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance,
                                                      EdsAsset edsAsset, User entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            List<AttachedPolicy> policies = awsIamPolicyRepo.listUserPolicies(instance.getEdsConfigModel(),
                    entity.getUserName());
            if (!CollectionUtils.isEmpty(policies)) {
                String policyName = policies.stream()
                        .map(AttachedPolicy::getPolicyName)
                        .collect(Collectors.joining(INDEX_VALUE_DIVISION_SYMBOL));
                indices.add(toEdsAssetIndex(edsAsset, AWS_IAM_POLICIES, policyName));
            }
        } catch (Exception e) {
            log.error("Failed to list policies for user: {}", entity.getUserName(), e);
        }
        indices.add(toEdsAssetIndex(edsAsset, CLOUD_ACCOUNT_USERNAME, entity.getUserName()));
        return indices;
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}