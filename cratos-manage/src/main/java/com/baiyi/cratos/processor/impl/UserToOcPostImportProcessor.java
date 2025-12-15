package com.baiyi.cratos.processor.impl;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.loader.EdsOpscloudConfigLoader;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.eds.opscloud.param.OcUserParam;
import com.baiyi.cratos.eds.opscloud.repo.OcUserRepo;
import com.baiyi.cratos.processor.BasePostImportAssetProcessor;
import com.baiyi.cratos.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/18 13:48
 * &#064;Version 1.0
 */
@Slf4j
@Order(2)
@Component
@RequiredArgsConstructor
public class UserToOcPostImportProcessor implements BasePostImportAssetProcessor {

    private final UserService userService;
    private final EdsInstanceQueryHelper edsInstanceQueryHelper;
    private final EdsIdentityFacade edsIdentityFacade;
    private final EdsOpscloudConfigLoader edsOpscloudConfigLoader;

    @Override
    public BusinessTypeEnum getBusinessType() {
        return BusinessTypeEnum.USER;
    }

    @Override
    public EdsAssetTypeEnum fromAssetType() {
        return EdsAssetTypeEnum.DINGTALK_USER;
    }

    @Override
    public void process(Integer businessId, EdsAsset asset, Map<String, Object> context) {
        User user = userService.getById(businessId);
        // 创建Ldap用户
        List<EdsInstance> opscloudInstances = edsInstanceQueryHelper.queryInstance(EdsInstanceTypeEnum.OPSCLOUD);
        if (CollectionUtils.isEmpty(opscloudInstances)) {
            return;
        }
        final String password = context.containsKey("PASSWORD") ? context.get("PASSWORD")
                .toString() : PasswordGenerator.generatePassword();
        opscloudInstances.forEach(opscloudInstance -> {
            EdsConfigs.Opscloud opscloud = edsOpscloudConfigLoader.getConfig(
                    opscloudInstance.getConfigId());
            OcUserParam.AddUser addUser = OcUserParam.AddUser.builder()
                    .email(user.getEmail())
                    .phone(user.getMobilePhone())
                    .displayName(user.getDisplayName())
                    .name(user.getName())
                    .password(password)
                    .username(user.getUsername())
                    .build();
            log.debug("create opscloud identity: {}", addUser);
            try {
                OcUserRepo.addUser(opscloud, addUser);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

}
