package com.baiyi.cratos.facade.permission.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.facade.permission.UserPermissionFacade;
import com.baiyi.cratos.service.EnvService;
import com.baiyi.cratos.service.UserPermissionService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.UserPermissionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    private final EnvService envService;

    @Override
    public DataTable<UserPermissionVO.Permission> queryUserPermissionPage(
            UserPermissionParam.UserPermissionPageQuery pageQuery) {
        DataTable<UserPermission> table = userPermissionService.queryUserPermissionPage(pageQuery);
        return userPermissionWrapper.wrapToTarget(table);
    }

    @Override
    public void grantUserPermission(UserPermissionParam.GrantUserPermission grantUserPermission) {
        UserPermission userPermission = grantUserPermission.toTarget();
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
        List<Env> envs = envService.selectAll()
                .stream()
                .filter(Env::getValid)
                .sorted(Comparator.comparing(Env::getSeq))
                .toList();
        SimpleBusiness hasBusiness = SimpleBusiness.builder()
                .businessType(queryBusinessUserPermissionDetails.getBusinessType())
                .businessId(queryBusinessUserPermissionDetails.getBusinessId())
                .build();
        UserPermissionVO.UserPermissionBusiness userPermissionBusiness = queryUserPermissionBusiness(username, envs,
                hasBusiness);
        return UserPermissionVO.UserPermissionDetails.builder()
                .userPermissions(List.of(userPermissionBusiness))
                .build();
    }

    private UserPermissionVO.UserPermissionBusiness queryUserPermissionBusiness(String username, List<Env> envs,
                                                                                SimpleBusiness hasBusiness) {
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
                .name(userPermissions.getFirst().getName())
                .displayName(userPermissions.getFirst().getDisplayName())
                .userPermissions(BeanCopierUtil.copyListProperties(userPermissions, UserPermissionVO.Permission.class))
                .build();
    }

    public UserPermissionVO.UserPermissionDetails queryUserPermissionDetails(String username, String businessType) {
        List<Integer> userPermissionBusinessIds = userPermissionService.queryUserPermissionBusinessIds(username,
                businessType);
        if (CollectionUtils.isEmpty(userPermissionBusinessIds)) {
            return UserPermissionVO.UserPermissionDetails.EMPTY;
        }
        List<Env> envs = envService.selectAll()
                .stream()
                .filter(Env::getValid)
                .sorted(Comparator.comparing(Env::getSeq))
                .toList();
        List<UserPermissionVO.UserPermissionBusiness> userPermissionBusinesses = userPermissionBusinessIds.stream()
                .map(id -> {
                    SimpleBusiness hasBusiness = SimpleBusiness.builder()
                            .businessType(businessType)
                            .businessId(id)
                            .build();
                    return queryUserPermissionBusiness(username, envs, hasBusiness);
                })
                .toList();

        return UserPermissionVO.UserPermissionDetails.builder()
                .userPermissions(userPermissionBusinesses)
                .build();
    }

}
