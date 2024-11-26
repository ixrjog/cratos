package com.baiyi.cratos.eds.aliyun.provider.ram;

import com.aliyuncs.ram.model.v20150501.*;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtil;
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
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_RAM_USERS;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/9 下午4:50
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_RAM_POLICY)
public class EdsAliyunRamPolicyAssetProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, GetPolicyResponse.Policy> {

    private final AliyunRamPolicyRepo aliyunRamPolicyRepo;

    private final AliyunRamUserRepo aliyunRamUserRepo;

    public EdsAliyunRamPolicyAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                           CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                           EdsAssetIndexFacade edsAssetIndexFacade,
                                           AliyunRamPolicyRepo aliyunRamPolicyRepo, AliyunRamUserRepo aliyunRamUserRepo,
                                           UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
        this.aliyunRamPolicyRepo = aliyunRamPolicyRepo;
        this.aliyunRamUserRepo = aliyunRamUserRepo;
    }

    @Override
    protected List<GetPolicyResponse.Policy> listEntities(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            List<ListPoliciesResponse.Policy> policies = aliyunRamPolicyRepo.listPolicies(instance.getEdsConfigModel());
            if (CollectionUtils.isEmpty(policies)) {
                return Collections.emptyList();
            } else {
                List<GetPolicyResponse.Policy> entities = Lists.newArrayList();
                for (ListPoliciesResponse.Policy policy : policies) {
                    entities.add(aliyunRamPolicyRepo.getPolicy(instance.getEdsConfigModel(), policy));
                }
                return entities;
            }
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  GetPolicyResponse.Policy entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getPolicyName())
                .kindOf(entity.getPolicyType())
                .descriptionOf(entity.getDescription())
                .createdTimeOf(TimeUtil.toDate(entity.getCreateDate(), TimeZoneEnum.UTC))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                                      EdsAsset edsAsset, GetPolicyResponse.Policy entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            List<ListEntitiesForPolicyResponse.User> users = aliyunRamUserRepo.listUsersForPolicy(
                    instance.getEdsConfigModel(), entity.getPolicyName(), entity.getPolicyType());
            if (!CollectionUtils.isEmpty(users)) {
                final String policyName = Joiner.on(INDEX_VALUE_DIVISION_SYMBOL)
                        .join(users.stream()
                                .map(ListEntitiesForPolicyResponse.User::getUserName)
                                .toList());
                indices.add(toEdsAssetIndex(edsAsset, ALIYUN_RAM_USERS, policyName));
            }
        } catch (Exception ignored) {
        }
        return indices;
    }

}