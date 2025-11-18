package com.baiyi.cratos.processor.impl;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.processor.BasePostImportAssetProcessor;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.workorder.notice.ResetUserPasswordNoticeSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/17 16:50
 * &#064;Version 1.0
 */
@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class UserToLdapPostImportProcessor implements BasePostImportAssetProcessor {

    private final UserService userService;
    private final EdsInstanceQueryHelper edsInstanceQueryHelper;
    private final EdsIdentityFacade edsIdentityFacade;
    private final ResetUserPasswordNoticeSender resetUserPasswordNoticeSender;

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
        List<EdsInstance> ldapInstances = edsInstanceQueryHelper.queryInstance(EdsInstanceTypeEnum.LDAP);
        if (CollectionUtils.isEmpty(ldapInstances)) {
            return;
        }
        final String password = context.containsKey("PASSWORD") ? context.get("PASSWORD")
                .toString() : PasswordGenerator.generatePassword();
        ldapInstances.forEach(ldapInstance -> {
            EdsIdentityParam.QueryLdapIdentityDetails queryLdapIdentityDetails = EdsIdentityParam.QueryLdapIdentityDetails.builder()
                    .username(user.getUsername())
                    .instanceId(ldapInstance.getId())
                    .build();
            EdsIdentityVO.LdapIdentityDetails ldapIdentityDetails = edsIdentityFacade.queryLdapIdentityDetails(
                    queryLdapIdentityDetails);
            // 确认身份不存在后创建
            List<EdsIdentityVO.LdapIdentity> ldapIdentities = Optional.ofNullable(ldapIdentityDetails)
                    .map(EdsIdentityVO.LdapIdentityDetails::getLdapIdentities)
                    .orElse(List.of());
            if (CollectionUtils.isEmpty(ldapIdentities)) {
                EdsIdentityParam.CreateLdapIdentity createLdapIdentity = EdsIdentityParam.CreateLdapIdentity.builder()
                        .instanceId(ldapInstance.getId())
                        .username(user.getUsername())
                        .password(password)
                        .build();
                log.info("create ldap identity: {}", createLdapIdentity);
                EdsIdentityVO.LdapIdentity ldapIdentity = edsIdentityFacade.createLdapIdentity(createLdapIdentity);
                resetUserPasswordNoticeSender.sendMsg(user.getUsername(), password);
            }
        });
    }

}
