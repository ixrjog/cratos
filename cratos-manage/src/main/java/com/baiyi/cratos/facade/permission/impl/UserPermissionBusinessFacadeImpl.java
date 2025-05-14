package com.baiyi.cratos.facade.permission.impl;

import com.baiyi.cratos.business.PermissionBusinessServiceFactory;
import com.baiyi.cratos.common.exception.UserPermissionBusinessException;
import com.baiyi.cratos.common.util.EnvLifecycleUtils;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.enums.RemoteManagementProtocolEnum;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.facade.SimpleEdsAccountFacade;
import com.baiyi.cratos.facade.permission.UserPermissionFacade;
import com.baiyi.cratos.domain.query.EdsAssetQuery;
import com.baiyi.cratos.query.ServerAccountQuery;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.domain.facade.UserPermissionBusinessFacade;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 10:11
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserPermissionBusinessFacadeImpl implements UserPermissionBusinessFacade {

    private final UserPermissionService userPermissionService;
    private final UserPermissionFacade userPermissionFacade;
    private final EnvFacade envFacade;
    private final EdsAssetService edsAssetService;
    private final TagGroupService tagGroupService;
    private final SimpleEdsAccountFacade simpleEdsAccountFacade;
    private final TagService tagService;
    private final BusinessTagService businessTagService;
    private final ServerAccountService serverAccountService;
    private static final List<EdsAssetTypeEnum> EFFECTIVE_ASSET_TYPES = List.of(EdsAssetTypeEnum.ALIYUN_ECS,
            EdsAssetTypeEnum.AWS_EC2, EdsAssetTypeEnum.HUAWEICLOUD_ECS, EdsAssetTypeEnum.CRATOS_COMPUTER);
    private final Tag edsTagUniqueKey = Tag.builder()
            .tagKey("EDS")
            .build();

    @Override
    public DataTable<PermissionBusinessVO.PermissionBusiness> queryUserPermissionBusinessPage(
            UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery) {
        PermissionBusinessServiceFactory.trySupport(pageQuery.getBusinessType());
        return PermissionBusinessServiceFactory.getService(pageQuery.getBusinessType())
                .queryUserPermissionBusinessPage(pageQuery);
    }

    @Override
    public void updateUserPermissionBusiness(
            UserPermissionBusinessParam.UpdateUserPermissionBusiness updateUserPermissionBusiness) {
        Map<String, Env> envLifecycleMap = envFacade.getEnvMap();
        for (UserPermissionBusinessParam.BusinessPermission businessPermission : updateUserPermissionBusiness.getBusinessPermissions()) {
            updateUserPermissionBusiness(updateUserPermissionBusiness.getUsername(),
                    updateUserPermissionBusiness.getBusinessType(), businessPermission, envLifecycleMap);
        }
    }

    private void updateUserPermissionBusiness(String username, String businessType,
                                              UserPermissionBusinessParam.BusinessPermission businessPermission,
                                              Map<String, Env> envLifecycleMap) {
        PermissionBusinessVO.PermissionBusiness permissionBusiness = PermissionBusinessServiceFactory.getService(
                        businessType)
                .getByBusinessPermission(businessPermission);
        if (Objects.isNull(permissionBusiness)) {
            throw new UserPermissionBusinessException(
                    "The authorized business object does not exist. businessType={}, businessId={}", businessType,
                    businessPermission.getBusinessId());
        }
        SimpleBusiness hasBusiness = SimpleBusiness.builder()
                .businessType(businessType)
                .businessId(permissionBusiness.getBusinessId())
                .build();
        Map<String, UserPermission> userPermissionMap = queryUserPermissionMap(username, hasBusiness);
        businessPermission.getRoleMembers()
                .forEach(roleMember -> {
                    if (roleMember.getChecked()) {
                        grantPermission(username, businessType, businessPermission, permissionBusiness, envLifecycleMap,
                                userPermissionMap, roleMember);
                    } else {
                        revokePermission(userPermissionMap, roleMember);
                    }
                });
    }

    private void grantPermission(String username, String businessType,
                                 UserPermissionBusinessParam.BusinessPermission businessPermission,
                                 PermissionBusinessVO.PermissionBusiness permissionBusiness,
                                 Map<String, Env> envLifecycleMap, Map<String, UserPermission> userPermissionMap,
                                 UserPermissionBusinessParam.RoleMember roleMember) {
        Date expiredTime = EnvLifecycleUtils.generateExpiredTimeWithEnvLifecycle(roleMember.getExpiredTime(),
                roleMember.getRole(), envLifecycleMap);
        if (userPermissionMap.containsKey(roleMember.getRole())) {
            UserPermission userPermission = userPermissionMap.get(roleMember.getRole());
            userPermission.setValid(true);
            userPermission.setExpiredTime(expiredTime);
            userPermissionService.updateByPrimaryKey(userPermission);
        } else {
            UserPermission userPermission = UserPermission.builder()
                    .role(roleMember.getRole())
                    .name(permissionBusiness.getName())
                    .displayName(permissionBusiness.getDisplayName())
                    .username(username)
                    .valid(true)
                    .expiredTime(expiredTime)
                    .businessType(businessType)
                    .businessId(Objects.isNull(businessPermission.getBusinessId()) ? businessPermission.getName()
                            .hashCode() : businessPermission.getBusinessId())
                    .seq(1)
                    .build();
            userPermissionService.add(userPermission);
        }
    }

    private void revokePermission(Map<String, UserPermission> userPermissionMap,
                                  UserPermissionBusinessParam.RoleMember roleMember) {
        if (userPermissionMap.containsKey(roleMember.getRole())) {
            userPermissionFacade.deleteUserPermissionById(userPermissionMap.get(roleMember.getRole())
                    .getId());
        }
    }

    private Map<String, UserPermission> queryUserPermissionMap(String username, BaseBusiness.HasBusiness hasBusiness) {
        return userPermissionService.queryUserPermissionByBusiness(username, hasBusiness)
                .stream()
                .collect(Collectors.toMap(UserPermission::getRole, a -> a, (k1, k2) -> k1));
    }

    @Override
    public PermissionBusinessVO.UserPermissionByBusiness queryUserPermissionByBusiness(
            UserPermissionParam.QueryUserPermissionByBusiness queryUserPermissionByBusiness) {
        List<String> usernames = userPermissionService.queryUserPermissionUsernames(queryUserPermissionByBusiness);
        if (CollectionUtils.isEmpty(usernames)) {
            return PermissionBusinessVO.UserPermissionByBusiness.EMPTY;
        }
        Map<String, List<UserPermissionVO.UserPermissionBusiness>> userPermissionsMap = Maps.newHashMap();
        usernames.forEach(username -> {
            UserPermissionParam.QueryBusinessUserPermissionDetails query = UserPermissionParam.QueryBusinessUserPermissionDetails.builder()
                    .businessId(queryUserPermissionByBusiness.getBusinessId())
                    .businessType(queryUserPermissionByBusiness.getBusinessType())
                    .username(username)
                    .build();
            UserPermissionVO.UserPermissionDetails details = userPermissionFacade.queryUserPermissionDetails(query);
            userPermissionsMap.put(username, details.getUserPermissions());
        });
        return PermissionBusinessVO.UserPermissionByBusiness.builder()
                .userPermissionsMap(userPermissionsMap)
                .build();
    }

    // 查询用户授权的资产列表
    @SuppressWarnings("unchecked")
    @Override
    public DataTable<EdsAsset> queryUserPermissionAssets(EdsAssetQuery.UserPermissionPageQueryParam param) {
        // 查询用户所有的授权组
        // TODO 管理员可以设置groups为空
        List<String> groups = userPermissionService.queryUserPermissionGroups(param.getUsername());
        if (CollectionUtils.isEmpty(groups)) {
            return DataTable.NO_DATA;
        }
        // 查询用户所有授权的资产
        Tag tagGroup = tagGroupService.getTagGroup();
        if (Objects.isNull(tagGroup)) {
            return DataTable.NO_DATA;
        }
        EdsAssetQuery.QueryUserPermissionBusinessIdParam query = EdsAssetQuery.QueryUserPermissionBusinessIdParam.builder()
                .username(param.getUsername())
                .tagGroupId(tagGroup.getId())
                .groups(groups)
                .build();
        // 用户授权的业务对象
        List<Integer> businessIds = edsAssetService.queryUserPermissionBusinessIds(query);
        if (CollectionUtils.isEmpty(businessIds)) {
            return DataTable.NO_DATA;
        }
        param.setEffectiveAssetTypes(EFFECTIVE_ASSET_TYPES.stream()
                .map(Enum::name)
                .toList());
        param.setUserPermissionIds(businessIds);
        return edsAssetService.queryUserPermissionPage(param);
    }

    @Override
    public List<ServerAccount> queryUserPermissionServerAccounts(String username) {
        List<Integer> userPermissionIds = userPermissionService.queryUserPermissionBusinessIds(username,
                BusinessTypeEnum.SERVER_ACCOUNT.name());
        if (CollectionUtils.isEmpty(userPermissionIds)) {
            return List.of();
        }
        return queryServerAccounts(userPermissionIds);
    }

    private List<ServerAccount> queryServerAccounts(List<Integer> userPermissionIds) {
        Tag edsTag = tagService.getByUniqueKey(edsTagUniqueKey);
        if (edsTag == null) {
            return Collections.emptyList();
        }
        List<BusinessTag> businessTags = businessTagService.queryByBusinessTypeAndTagId(
                BusinessTypeEnum.SERVER_ACCOUNT.name(), edsTag.getId());
        ServerAccountQuery.QueryUserPermissionServerAccountParam param = ServerAccountQuery.QueryUserPermissionServerAccountParam.builder()
                .userPermissionIds(userPermissionIds)
                .build();
        List<ServerAccount> serverAccounts = serverAccountService.queryUserPermissionServerAccounts(param);
        return serverAccounts.stream()
                .filter(e -> RemoteManagementProtocolEnum.SSH.name()
                        .equals(e.getProtocol()))
                .toList();
    }

}
