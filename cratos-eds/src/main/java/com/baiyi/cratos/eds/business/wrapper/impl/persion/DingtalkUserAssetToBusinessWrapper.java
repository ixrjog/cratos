package com.baiyi.cratos.eds.business.wrapper.impl.persion;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.util.UsernameUtils;
import com.baiyi.cratos.eds.business.wrapper.impl.BaseAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkUserModel;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/17 13:40
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.USER)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.DINGTALK_APP, assetTypeOf = EdsAssetTypeEnum.DINGTALK_USER)
public class DingtalkUserAssetToBusinessWrapper extends BaseAssetToBusinessWrapper<User, DingtalkUserModel.User> {

    public DingtalkUserAssetToBusinessWrapper(BusinessAssetBoundService businessAssetBoundService) {
        super(businessAssetBoundService);
    }

    @Override
    protected User toTarget(EdsAssetVO.Asset asset) {
        DingtalkUserModel.User model = getAssetModel(asset);
        // 用户名全部使用小写字符，首选邮箱用户名转换
        String username = getUsername(model);
        return User.builder()
                .username(username)
                .name(model.getName())
                .displayName(model.getName())
                .email(model.getEmail())
                .mobilePhone(model.getMobile())
                .valid(true)
                .locked(false)
                .build();
    }

    private String getUsername(DingtalkUserModel.User model) {
        String email = Optional.of(model)
                .map(DingtalkUserModel.User::getEmail)
                .orElse("");
        if (StringUtils.hasText(email)) {
            return UsernameUtils.ofEmail(email);
        }
        return UsernameUtils.ofName(model.getName());
    }

}
