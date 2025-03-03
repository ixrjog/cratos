package com.baiyi.cratos.eds.aliyun.provider.ram;

import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamPolicyRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_RAM_POLICIES;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCOUNT_USERNAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/9 下午4:10
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_RAM_USER)
public class EdsAliyunRamUserAssetProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> {

    private final AliyunRamUserRepo aliyunRamUserRepo;
    private final AliyunRamPolicyRepo aliyunRamPolicyRepo;

    public EdsAliyunRamUserAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                         CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                         EdsAssetIndexFacade edsAssetIndexFacade,
                                         UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                         EdsInstanceProviderHolderBuilder holderBuilder,
                                         AliyunRamUserRepo aliyunRamUserRepo, AliyunRamPolicyRepo aliyunRamPolicyRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.aliyunRamUserRepo = aliyunRamUserRepo;
        this.aliyunRamPolicyRepo = aliyunRamPolicyRepo;
    }

    @Override
    protected List<GetUserResponse.User> listEntities(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            List<ListUsersResponse.User> users = aliyunRamUserRepo.listUsers(instance.getEdsConfigModel());
            if (CollectionUtils.isEmpty(users)) {
                return Collections.emptyList();
            } else {
                List<GetUserResponse.User> entities = Lists.newArrayList();
                for (ListUsersResponse.User user : users) {
                    entities.add(aliyunRamUserRepo.getUser(instance.getEdsConfigModel(), user.getUserName()));
                }
                return entities;
            }
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  GetUserResponse.User entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getUserId())
                .nameOf(entity.getDisplayName())
                .assetKeyOf(entity.getUserName())
                .descriptionOf(entity.getComments())
                .createdTimeOf(TimeUtils.toDate(entity.getCreateDate(), TimeZoneEnum.UTC))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                                      EdsAsset edsAsset, GetUserResponse.User entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            List<ListPoliciesForUserResponse.Policy> policies = aliyunRamPolicyRepo.listPoliciesForUser(
                    instance.getEdsConfigModel(), entity.getUserName());
            if (!CollectionUtils.isEmpty(policies)) {
                String policyName = policies.stream()
                        .map(ListPoliciesForUserResponse.Policy::getPolicyName)
                        .collect(Collectors.joining(INDEX_VALUE_DIVISION_SYMBOL));
                indices.add(toEdsAssetIndex(edsAsset, ALIYUN_RAM_POLICIES, policyName));
            }
        } catch (Exception e) {
            log.error("Failed to list policies for user: {}", entity.getUserName(), e);
        }
        indices.add(toEdsAssetIndex(edsAsset, CLOUD_ACCOUNT_USERNAME, entity.getUserName()));
        return indices;
    }

}