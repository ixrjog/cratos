package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsCloudIdentityExtension;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.identity.extension.base.BaseEdsIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.cloud.CloudIdentityFactory;
import com.baiyi.cratos.facade.identity.extension.cloud.CloudIdentityProvider;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCOUNT_USERNAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 09:53
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class EdsCloudIdentityExtensionImpl extends BaseEdsIdentityExtension implements EdsCloudIdentityExtension {

    public EdsCloudIdentityExtensionImpl(EdsAssetWrapper edsAssetWrapper, EdsInstanceService edsInstanceService,
                                         EdsInstanceWrapper edsInstanceWrapper, UserService userService,
                                         UserWrapper userWrapper, EdsInstanceProviderHolderBuilder holderBuilder,
                                         EdsAssetService edsAssetService, EdsFacade edsFacade,
                                         EdsAssetIndexService edsAssetIndexService, TagService tagService,
                                         BusinessTagService businessTagService) {
        super(
                edsAssetWrapper, edsInstanceService, edsInstanceWrapper, userService, userWrapper, holderBuilder,
                edsAssetService, edsFacade, edsAssetIndexService, tagService, businessTagService
        );
    }

    private List<EdsAsset> queryAccountAssets(EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails) {
        List<EdsAsset> cloudIdentityAssets = Lists.newArrayList();
        List<EdsAsset> byIndexUsername = edsAssetIndexService.queryIndexByNameAndValue(
                        CLOUD_ACCOUNT_USERNAME,
                        queryCloudIdentityDetails.getUsername()
                )
                .stream()
                .map(e -> edsAssetService.getById(e.getAssetId()))
                .toList();
        cloudIdentityAssets.addAll(byIndexUsername);
        cloudIdentityAssets.addAll(queryByUsernameTag(
                queryCloudIdentityDetails.getUsername(), EdsAssetTypeEnum.getCloudIdentityTypes()
                        .stream()
                        .map(Enum::name)
                        .toList()
        ));
        return cloudIdentityAssets.stream()
                .collect(Collectors.toMap(EdsAsset::getId, asset -> asset, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

    @Override
    public EdsIdentityVO.CloudIdentityDetails queryCloudIdentityDetails(
            EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails) {
        User user = userService.getByUsername(queryCloudIdentityDetails.getUsername());
        List<EdsAsset> cloudIdentityAssets = queryAccountAssets(queryCloudIdentityDetails);
        if (cloudIdentityAssets.isEmpty()) {
            return EdsIdentityVO.CloudIdentityDetails.NO_DATA;
        }
        Map<Integer, EdsInstance> instanceMap = getEdsInstanceMap(cloudIdentityAssets, queryCloudIdentityDetails);
        Map<String, List<EdsIdentityVO.CloudAccount>> accounts = Maps.newHashMap();
        cloudIdentityAssets.stream()
                .filter(asset -> instanceMap.containsKey(asset.getInstanceId()))
                .forEach(asset -> {
                    CloudIdentityProvider cloudIdentityProvider = CloudIdentityFactory.getProvider(
                            instanceMap.get(asset.getInstanceId())
                                    .getEdsType());
                    EdsIdentityVO.CloudAccount cloudAccount = cloudIdentityProvider.getAccount(
                            instanceMap.get(asset.getInstanceId()), user, getAccountUsername(asset));
                    putAccounts(accounts, cloudAccount);
                });
        return EdsIdentityVO.CloudIdentityDetails.builder()
                .username(queryCloudIdentityDetails.getUsername())
                .accounts(accounts)
                .build();
    }

    private String getAccountUsername(EdsAsset asset) {
        EdsAssetIndex uk = EdsAssetIndex.builder()
                .instanceId(asset.getInstanceId())
                .assetId(asset.getId())
                .name(CLOUD_ACCOUNT_USERNAME)
                .build();
        EdsAssetIndex usernameIndex = edsAssetIndexService.getByUniqueKey(uk);
        return usernameIndex.getValue();
    }

    private void putAccounts(Map<String, List<EdsIdentityVO.CloudAccount>> accounts,
                             EdsIdentityVO.CloudAccount cloudAccount) {
        accounts.computeIfAbsent(
                        cloudAccount.getInstance()
                                .getEdsType(), k -> Lists.newArrayList()
                )
                .add(cloudAccount);
    }

    protected EdsInstance getAndVerifyEdsInstance(HasEdsInstanceId hasEdsInstanceId,
                                                  EdsInstanceTypeEnum instanceTypeEnum) {
        if (!IdentityUtils.hasIdentity(hasEdsInstanceId.getInstanceId())) {
            EdsIdentityException.runtime("{} instanceId is incorrect.", instanceTypeEnum.name());
        }
        EdsInstance instance = edsInstanceService.getById(hasEdsInstanceId.getInstanceId());
        if (Objects.isNull(instance)) {
            EdsIdentityException.runtime("{} instance does not exist.", instanceTypeEnum.name());
        }
        return instance;
    }

    @Override
    public EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount) {
        EdsInstance instance = getAndVerifyEdsInstance(createCloudAccount);
        CloudIdentityProvider cloudIdentityProvider = CloudIdentityFactory.getProvider(instance.getEdsType());
        return cloudIdentityProvider.createCloudAccount(instance, createCloudAccount);
    }

    @Override
    public void grantCloudAccountPermission(EdsIdentityParam.GrantPermission grantPermission) {
        EdsInstance instance = getAndVerifyEdsInstance(grantPermission);
        CloudIdentityProvider cloudIdentityProvider = CloudIdentityFactory.getProvider(instance.getEdsType());
        cloudIdentityProvider.grantPermission(instance, grantPermission);
    }

    @Override
    public void revokeCloudAccountPermission(EdsIdentityParam.RevokePermission revokePermission) {
        EdsInstance instance = getAndVerifyEdsInstance(revokePermission);
        CloudIdentityProvider cloudIdentityProvider = CloudIdentityFactory.getProvider(instance.getEdsType());
        cloudIdentityProvider.revokePermission(instance, revokePermission);
    }

    @Override
    public void blockCloudAccount(EdsIdentityParam.BlockCloudAccount blockCloudAccount) {
        EdsInstance instance = getAndVerifyEdsInstance(blockCloudAccount);
        CloudIdentityProvider cloudIdentityProvider = CloudIdentityFactory.getProvider(instance.getEdsType());
        cloudIdentityProvider.blockCloudAccount(instance, blockCloudAccount);
        cloudIdentityProvider.importCloudAccount(instance, blockCloudAccount);
    }

    private Map<String, Map<Integer, List<EdsAssetVO.Asset>>> makeCloudIdentities(List<EdsAsset> cloudIdentityAssets) {
        Map<String, Map<Integer, List<EdsAssetVO.Asset>>> cloudIdentities = Maps.newHashMap();
        cloudIdentityAssets.forEach(cloudIdentityAsset -> {
            String assetType = cloudIdentityAsset.getAssetType();
            EdsAssetVO.Asset assetVO = edsAssetWrapper.wrapToTarget(cloudIdentityAsset);
            cloudIdentities.computeIfAbsent(assetType, k -> Maps.newHashMap())
                    .computeIfAbsent(cloudIdentityAsset.getInstanceId(), k -> Lists.newArrayList())
                    .add(assetVO);
        });
        return cloudIdentities;
    }

}
