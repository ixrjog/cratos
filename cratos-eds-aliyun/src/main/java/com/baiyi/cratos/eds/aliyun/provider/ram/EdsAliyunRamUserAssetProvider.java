package com.baiyi.cratos.eds.aliyun.provider.ram;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.*;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamAccessKeyRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamPolicyRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/9 下午4:10
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_RAM_USER)
public class EdsAliyunRamUserAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Aliyun, GetUserResponse.User> {

    private final AliyunRamUserRepo aliyunRamUserRepo;
    private final AliyunRamPolicyRepo aliyunRamPolicyRepo;
    private final AliyunRamAccessKeyRepo aliyunRamAccessKeyRepo;

    public EdsAliyunRamUserAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                         CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                         EdsAssetIndexFacade edsAssetIndexFacade,
                                         AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                         EdsInstanceProviderHolderBuilder holderBuilder,
                                         AliyunRamUserRepo aliyunRamUserRepo, AliyunRamPolicyRepo aliyunRamPolicyRepo,
                                         AliyunRamAccessKeyRepo aliyunRamAccessKeyRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
        this.aliyunRamUserRepo = aliyunRamUserRepo;
        this.aliyunRamPolicyRepo = aliyunRamPolicyRepo;
        this.aliyunRamAccessKeyRepo = aliyunRamAccessKeyRepo;
    }

    @Override
    protected List<GetUserResponse.User> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            List<ListUsersResponse.User> users = aliyunRamUserRepo.listUsers(instance.getConfig());
            if (CollectionUtils.isEmpty(users)) {
                return Collections.emptyList();
            } else {
                List<GetUserResponse.User> entities = Lists.newArrayList();
                for (ListUsersResponse.User user : users) {
                    entities.add(aliyunRamUserRepo.getUser(instance.getConfig(), user.getUserName()));
                }
                return entities;
            }
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                  GetUserResponse.User entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getUserId())
                .nameOf(entity.getDisplayName())
                .assetKeyOf(entity.getUserName())
                .descriptionOf(entity.getComments())
                .createdTimeOf(TimeUtils.toDate(entity.getCreateDate(), TimeZoneEnum.UTC))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                            EdsAsset edsAsset, GetUserResponse.User entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            List<ListPoliciesForUserResponse.Policy> policies = aliyunRamPolicyRepo.listPoliciesForUser(
                    instance.getConfig(), entity.getUserName());
            if (!CollectionUtils.isEmpty(policies)) {
                String policyName = policies.stream()
                        .map(ListPoliciesForUserResponse.Policy::getPolicyName)
                        .collect(Collectors.joining(INDEX_VALUE_DIVISION_SYMBOL));
                indices.add(createEdsAssetIndex(edsAsset, ALIYUN_RAM_POLICIES, policyName));
            }
        } catch (Exception e) {
            log.error("Failed to list policies for user: {}", entity.getUserName(), e);
        }
        // accessKeys
        try {
            List<ListAccessKeysResponse.AccessKey> accessKeys = aliyunRamAccessKeyRepo.listAccessKeys(
                    instance.getConfig(), entity.getUserName());
            if (!CollectionUtils.isEmpty(accessKeys)) {
                final String accessKeyIds = accessKeys.stream()
                        .map(ListAccessKeysResponse.AccessKey::getAccessKeyId)
                        .collect(Collectors.joining(INDEX_VALUE_DIVISION_SYMBOL));
                indices.add(createEdsAssetIndex(edsAsset, CLOUD_ACCESS_KEY_IDS, accessKeyIds));
            }
        } catch (Exception ignored) {
        }
        // loginProfile
        try {
            GetLoginProfileResponse.LoginProfile loginProfile = aliyunRamUserRepo.getLoginProfile(
                    instance.getConfig()
                            .getRegionId(), instance.getConfig(), entity.getUserName());
            if (Objects.nonNull(loginProfile)) {
                indices.add(createEdsAssetIndex(edsAsset, CLOUD_LOGIN_PROFILE, "Enabled"));
            }
        } catch (ClientException ignored) {
        }
        indices.add(createEdsAssetIndex(edsAsset, CLOUD_ACCOUNT_USERNAME, entity.getUserName()));
        return indices;
    }

    @Override
    protected boolean isAssetChanged(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.builder()
                .comparisonName(true)
                .comparisonKey(true)
                .comparisonDescription(true)
                .comparisonExpiredTime(true)
                .comparisonOriginalModel(true)
                .comparisonKind(true)
                .build()
                .compare(a1, a2);
    }

}