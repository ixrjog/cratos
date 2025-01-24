package com.baiyi.cratos.facade.permission.impl;

import com.baiyi.cratos.business.PermissionBusinessServiceFactory;
import com.baiyi.cratos.common.exception.UserPermissionBusinessException;
import com.baiyi.cratos.common.util.EnvLifecycleUtils;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.facade.permission.UserPermissionBusinessFacade;
import com.baiyi.cratos.facade.permission.UserPermissionFacade;
import com.baiyi.cratos.service.UserPermissionService;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
                .getByBusinessId(businessPermission.getBusinessId());
        if (permissionBusiness == null) {
            // 授权对象不存在
            throw new UserPermissionBusinessException(
                    "The authorized business object does not exist. businessType={}, businessId={}", businessType,
                    businessPermission.getBusinessId());
        }
        SimpleBusiness hasBusiness = SimpleBusiness.builder()
                .businessType(businessType)
                .businessId(businessPermission.getBusinessId())
                .build();
        Map<String, UserPermission> userPermissionMap = queryUserPermissionMap(username, hasBusiness);
        for (UserPermissionBusinessParam.RoleMember roleMember : businessPermission.getRoleMembers()) {
            if (roleMember.getChecked()) {
                Date expiredTime = EnvLifecycleUtils.generateExpiredTimeWithEnvLifecycle(roleMember.getExpiredTime(),
                        roleMember.getRole(), envLifecycleMap);
                // grant
                if (userPermissionMap.containsKey(roleMember.getRole())) {
                    // update
                    UserPermission userPermission = userPermissionMap.get(roleMember.getRole());
                    userPermission.setValid(true);
                    userPermission.setExpiredTime(expiredTime);
                    userPermissionService.updateByPrimaryKey(userPermission);
                } else {
                    // add
                    UserPermission userPermission = UserPermission.builder()
                            .role(roleMember.getRole())
                            .name(permissionBusiness.getName())
                            .displayName(permissionBusiness.getDisplayName())
                            .username(username)
                            .valid(true)
                            .expiredTime(expiredTime)
                            .businessType(businessType)
                            .businessId(businessPermission.getBusinessId())
                            .seq(1)
                            .build();
                    userPermissionService.add(userPermission);
                }
            } else {
                // revoke
                if (userPermissionMap.containsKey(roleMember.getRole())) {
                    userPermissionFacade.deleteUserPermissionById(userPermissionMap.get(roleMember.getRole())
                            .getId());
                }
            }
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

}
