package com.baiyi.cratos.facade.permission.impl;

import com.baiyi.cratos.business.PermissionBusinessService;
import com.baiyi.cratos.business.PermissionBusinessServiceFactory;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.LifeCycleVO;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.facade.UserPermissionFacade;
import com.baiyi.cratos.service.UserPermissionService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EnvWrapper;
import com.baiyi.cratos.wrapper.UserPermissionWrapper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/1/18 18:18
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserPermissionFacadeImpl implements UserPermissionFacade {

    private final UserPermissionService userPermissionService;
    private final UserPermissionWrapper userPermissionWrapper;
    private final UserService userService;
    private final EnvFacade envFacade;
    private final EnvWrapper envWrapper;

    @Override
    public DataTable<UserPermissionVO.Permission> queryUserPermissionPage(
            UserPermissionParam.UserPermissionPageQuery pageQuery) {
        DataTable<UserPermission> table = userPermissionService.queryUserPermissionPage(pageQuery);
        return userPermissionWrapper.wrapToTarget(table);
    }

    @Override
    public void grantUserPermission(UserPermissionParam.GrantUserPermission grantUserPermission) {
        PermissionBusinessService<?> service = PermissionBusinessServiceFactory.getService(
                grantUserPermission.getBusinessType());
        PermissionBusinessVO.PermissionBusiness permissionBusiness = service.getByBusinessId(
                grantUserPermission.getBusinessId());
        UserPermission userPermission = UserPermission.builder()
                .username(grantUserPermission.getUsername())
                .name(permissionBusiness.getName())
                .businessId(grantUserPermission.getBusinessId())
                .businessType(grantUserPermission.getBusinessType())
                .displayName(permissionBusiness.getDisplayName())
                .role(grantUserPermission.getRole())
                .valid(Global.VALID)
                .build();
        if (userPermissionService.getByUniqueKey(userPermission) == null) {
            userPermissionService.add(userPermission);
        }
    }

    @Override
    public void revokeUserPermission(UserPermissionParam.RevokeUserPermission revokeUserPermission) {
        UserPermission uniqueKey = revokeUserPermission.toTarget();
        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);
        if (userPermission != null) {
            this.deleteUserPermissionById(userPermission.getId());
        }
    }

    @Override
    public void deleteUserPermissionById(int id) {
        userPermissionService.deleteById(id);
    }

    @Override
    public boolean contains(String username, BaseBusiness.HasBusiness hasBusiness) {
        return userPermissionService.contains(username, hasBusiness);
    }

    @Override
    public boolean contains(String username, BaseBusiness.HasBusiness hasBusiness, String role) {
        return userPermissionService.contains(username, hasBusiness, role);
    }

    @Override
    public UserPermissionVO.UserPermissionDetails queryUserPermissionDetails(
            UserPermissionParam.QueryBusinessUserPermissionDetails queryBusinessUserPermissionDetails) {
        String username = queryBusinessUserPermissionDetails.getUsername();
        List<Env> envs = envFacade.querySorted();
        UserPermissionVO.UserPermissionBusiness userPermissionBusiness = queryUserPermissionBusiness(username, envs,
                queryBusinessUserPermissionDetails);
        return UserPermissionVO.UserPermissionDetails.builder()
                .userPermissions(List.of(userPermissionBusiness))
                .build();
    }

    private UserPermissionVO.UserPermissionBusiness queryUserPermissionBusiness(String username, List<Env> envs,
                                                                                BaseBusiness.HasBusiness hasBusiness) {
        Map<String, UserPermission> userPermissionMap = userPermissionService.queryUserPermissionByBusiness(username,
                        hasBusiness)
                .stream()
                .collect(Collectors.toMap(UserPermission::getRole, a -> a, (k1, k2) -> k1));
        List<UserPermission> userPermissions = envs.stream()
                .map(env -> {
                    if (userPermissionMap.containsKey(env.getEnvName())) {
                        return userPermissionMap.get(env.getEnvName());
                    } else {
                        return UserPermission.builder()
                                .username(username)
                                .role(env.getEnvName())
                                .businessType(hasBusiness.getBusinessType())
                                .businessId(hasBusiness.getBusinessId())
                                .seq(env.getSeq())
                                .build();
                    }
                })
                .toList();
        return UserPermissionVO.UserPermissionBusiness.builder()
                .businessType(hasBusiness.getBusinessType())
                .businessId(hasBusiness.getBusinessId())
                .name(userPermissions.stream()
                        .filter(e -> StringUtils.hasText(e.getName()))
                        .findFirst()
                        .map(UserPermission::getName)
                        .orElse(""))
                .userPermissions(to(userPermissions))
                .build();
    }

    private List<UserPermissionVO.Permission> to(List<UserPermission> userPermissions) {
        return userPermissions.stream()
                .map(e -> {
                    UserPermissionVO.Permission permission = BeanCopierUtil.copyProperties(e,
                            UserPermissionVO.Permission.class);
                    LifeCycleVO.invoke(permission);
                    envWrapper.businessWrap(permission);
                    return permission;
                })
                .toList();
    }

    @Override
    public UserPermissionVO.UserPermissionDetails queryUserPermissionDetails(
            UserPermissionParam.QueryAllBusinessUserPermissionDetails queryAllBusinessUserPermissionDetails) {
        List<Integer> userPermissionBusinessIds = userPermissionService.queryUserPermissionBusinessIds(
                queryAllBusinessUserPermissionDetails.getUsername(),
                queryAllBusinessUserPermissionDetails.getBusinessType());
        if (CollectionUtils.isEmpty(userPermissionBusinessIds)) {
            return UserPermissionVO.UserPermissionDetails.NO_DATA;
        }
        List<Env> envs = envFacade.querySorted();
        List<UserPermissionVO.UserPermissionBusiness> userPermissionBusinesses = userPermissionBusinessIds.stream()
                .map(businessId -> {
                    SimpleBusiness hasBusiness = SimpleBusiness.builder()
                            .businessType(queryAllBusinessUserPermissionDetails.getBusinessType())
                            .businessId(businessId)
                            .build();
                    return queryUserPermissionBusiness(queryAllBusinessUserPermissionDetails.getUsername(), envs,
                            hasBusiness);
                })
                .toList();
        return UserPermissionVO.UserPermissionDetails.builder()
                .userPermissions(userPermissionBusinesses)
                .build();
    }

    @Override
    public UserPermissionVO.BusinessUserPermissionDetails getUserBusinessUserPermissionDetails(String username) {
        Set<String> businessTypes = PermissionBusinessServiceFactory.getBusinessTypes();
        // Map<@BusinessType String, List<UserPermissionVO.UserPermissionBusiness>>
        Map<String, List<UserPermissionVO.UserPermissionBusiness>> businessPermissions = Maps.newHashMap();
        businessTypes.forEach(businessType -> {
            UserPermissionParam.QueryAllBusinessUserPermissionDetails query = UserPermissionParam.QueryAllBusinessUserPermissionDetails.builder()
                    .username(username)
                    .businessType(businessType)
                    .build();
            UserPermissionVO.UserPermissionDetails userPermissionDetails = queryUserPermissionDetails(query);
            businessPermissions.put(businessType, userPermissionDetails.getUserPermissions());
        });
        return UserPermissionVO.BusinessUserPermissionDetails.builder()
                .businessPermissions(businessPermissions)
                .build();
    }

}
