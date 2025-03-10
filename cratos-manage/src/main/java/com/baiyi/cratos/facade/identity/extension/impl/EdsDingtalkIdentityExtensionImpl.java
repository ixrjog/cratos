package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.identity.extension.EdsDingtalkIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.base.BaseEdsIdentityExtension;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.DINGTALK_USER_MOBILE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/10 14:01
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class EdsDingtalkIdentityExtensionImpl  extends BaseEdsIdentityExtension implements EdsDingtalkIdentityExtension {

    public EdsDingtalkIdentityExtensionImpl(EdsAssetWrapper edsAssetWrapper, EdsInstanceService edsInstanceService,
                                            EdsInstanceWrapper edsInstanceWrapper, UserService userService,
                                            UserWrapper userWrapper, EdsInstanceProviderHolderBuilder holderBuilder,
                                            EdsAssetService edsAssetService, EdsFacade edsFacade,
                                            EdsAssetIndexService edsAssetIndexService) {
        super(edsAssetWrapper, edsInstanceService, edsInstanceWrapper, userService, userWrapper, holderBuilder,
                edsAssetService, edsFacade, edsAssetIndexService);
    }

    @Override
    public EdsIdentityVO.DingtalkIdentityDetails queryDingtalkIdentityDetails(
            EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails) {
        String username = queryDingtalkIdentityDetails.getUsername();
        User user = userService.getByUsername(username);
        if (Objects.isNull(user)) {
            return EdsIdentityVO.DingtalkIdentityDetails.NO_DATA;
        }
        List<EdsAsset> assets = onlyInTheInstance(queryDingtalkAssetByMobile(user.getMobilePhone()),
                queryDingtalkIdentityDetails);
        if (CollectionUtils.isEmpty(assets)) {
            return EdsIdentityVO.DingtalkIdentityDetails.NO_DATA;
        }
        List<EdsIdentityVO.DingtalkIdentity> dingtalkIdentities = assets.stream()
                .map(asset -> EdsIdentityVO.DingtalkIdentity.builder()
                        .username(username)
                        .user(userWrapper.wrapToTarget(user))
                        .instance(edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())))
                        .account(edsAssetWrapper.wrapToTarget(asset))
                        .build())
                .toList();
        return EdsIdentityVO.DingtalkIdentityDetails.builder()
                .username(username)
                .dingtalkIdentities(dingtalkIdentities)
                .build();
    }

    private List<EdsAsset> queryDingtalkAssetByMobile(String mobilePhone) {
        if (!StringUtils.hasText(mobilePhone)) {
            return List.of();
        }
        List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(DINGTALK_USER_MOBILE, mobilePhone);
        if (CollectionUtils.isEmpty(indices)) {
            return List.of();
        }
        return indices.stream()
                .map(e -> edsAssetService.getById(e.getAssetId()))
                .collect(Collectors.toMap(EdsAsset::getId, asset -> asset, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

}
