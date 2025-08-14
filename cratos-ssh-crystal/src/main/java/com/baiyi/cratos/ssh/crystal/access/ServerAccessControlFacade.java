package com.baiyi.cratos.ssh.crystal.access;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.ServerAccountService;
import com.baiyi.cratos.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/12 14:27
 * &#064;Version 1.0
 */
@SuppressWarnings({"rawtypes"})
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerAccessControlFacade {

    private final EdsAssetService edsAssetService;
    private final BusinessTagFacade businessTagFacade;
    private final UserPermissionService userPermissionService;
    private final ServerAccountService serverAccountService;

    public AccessControlVO.AccessControl generateAccessControl(String username, int assetId) {
        // Check if the server asset exists
        EdsAsset server = edsAssetService.getById(assetId);
        if (server == null) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(),
                    "Server does not exist");
        }
        // group tag
        BusinessTag groupBusinessTag = getServerBusinessTag(assetId, SysTagKeys.GROUP);
        if (groupBusinessTag == null) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(),
                    "Server has no group information");
        }
        String groupName = groupBusinessTag.getTagValue();
        // env tag
        BusinessTag envBusinessTag = getServerBusinessTag(assetId, SysTagKeys.ENV);
        if (envBusinessTag == null) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(),
                    "Server has no environment information");
        }
        String env = envBusinessTag.getTagValue();
        UserPermission userPermission = userPermissionService.getUserPermissionTagGroup(username, groupName, env);
        // Check user's server asset authorization
        if (userPermission == null || ExpiredUtils.isExpired(userPermission.getExpiredTime())) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(),
                    "Unauthorized or authorization expired");
        }
        // Check user's server account authorization
        BusinessTag accountBusinessTag = getServerBusinessTag(assetId, SysTagKeys.SERVER_ACCOUNT);
        if (accountBusinessTag == null || !StringUtils.hasText(accountBusinessTag.getTagValue())) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(),
                    "Server has no ServerAccount information");
        }
        String serverAccount = accountBusinessTag.getTagValue();
        ServerAccount account = serverAccountService.getByName(serverAccount);
        if (account == null) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(),
                    "ServerAccount does not exist");
        }
        if (!verifyAccountAuthorization(username, account, env)) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(),
                    "Unauthorized or authorization expired");
        }
        return AccessControlVO.AccessControl.authorized(BusinessTypeEnum.EDS_ASSET.name());
    }

    private boolean verifyAccountAuthorization(String username, ServerAccount account, String env) {
        UserPermission uk = UserPermission.builder()
                .username(username)
                .businessType(BusinessTypeEnum.SERVER_ACCOUNT.name())
                .name(account.getName())
                .businessId(account.getId())
                .role(env)
                .build();
        UserPermission userPermission = userPermissionService.getByUniqueKey(uk);
        return userPermission != null && !ExpiredUtils.isExpired(userPermission.getExpiredTime());
    }

    private boolean containsTagGroup(String username, String groupName, String namespace) {
        SimpleBusiness byBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.TAG_GROUP.name())
                .businessId(groupName.hashCode())
                .build();
        return userPermissionService.contains(username, byBusiness, namespace);
    }

    private BusinessTag getServerBusinessTag(int assetId, SysTagKeys sysTagKeys) {
        SimpleBusiness byBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(assetId)
                .build();
        return businessTagFacade.getBusinessTag(byBusiness, sysTagKeys.getKey());
    }

}
