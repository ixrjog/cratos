package com.baiyi.cratos.ssh.crystal.access;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    private final UserPermissionService userPermissionBusinessService;

    public AccessControlVO.AccessControl generateAccessControl(String username, int assetId) {
        // group标签
        BusinessTag groupBusinessTag = getServerBusinessTag(assetId, SysTagKeys.GROUP);
        if (groupBusinessTag == null) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(), "服务器没有组信息");
        }
        String groupName = groupBusinessTag.getTagValue();
        // env标签
        BusinessTag envBusinessTag = getServerBusinessTag(assetId, SysTagKeys.ENV);
        if (envBusinessTag == null) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(), "服务器没有环境信息");
        }
        String env = envBusinessTag.getTagValue();
        UserPermission userPermission = userPermissionBusinessService.getUserPermissionTagGroup(username, groupName,
                env);

        EdsAsset server = edsAssetService.getById(assetId);
        if (server == null) {
            return AccessControlVO.AccessControl.unauthorized(BusinessTypeEnum.EDS_ASSET.name(), "服务器不存在");
        }

        // TODO
        return null;
    }

    private boolean containsTagGroup(String username, String groupName, String namespace) {
        SimpleBusiness byBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.TAG_GROUP.name())
                .businessId(groupName.hashCode())
                .build();
        return userPermissionBusinessService.contains(username, byBusiness, namespace);
    }

    private BusinessTag getServerBusinessTag(int assetId, SysTagKeys sysTagKeys) {
        SimpleBusiness byBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(assetId)
                .build();
        return businessTagFacade.getBusinessTag(byBusiness, sysTagKeys.getKey());
    }


}
