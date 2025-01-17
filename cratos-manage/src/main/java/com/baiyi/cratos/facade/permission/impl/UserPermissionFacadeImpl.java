package com.baiyi.cratos.facade.permission.impl;

import com.baiyi.cratos.common.merger.BusinessUserPermissionMerger;
import com.baiyi.cratos.common.merger.UserPermissionMerger;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.facade.permission.UserPermissionFacade;
import com.baiyi.cratos.service.EnvService;
import com.baiyi.cratos.service.UserPermissionService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.UserPermissionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public UserPermissionVO.UserPermissionDetails getUserPermissionDetailsByUsername(String username) {
//        List<Env> envs = envService.selectAll()
//                .stream()
//                .filter(Env::getValid)
//                .sorted(Comparator.comparing(Env::getSeq))
//                .toList();
        return UserPermissionVO.UserPermissionDetails.builder()
                .permissions(UserPermissionMerger.newMerger()
                        .withUserPermissions(userPermissionService.queryByUsername(username))
                        .make())
                .build();
    }

    @Override
    public UserPermissionVO.BusinessUserPermissionDetails queryBusinessUserPermissionDetails(
            UserPermissionParam.QueryBusinessUserPermissionDetails queryBusinessUserPermissionDetails) {
        List<UserPermission> userPermissions = userPermissionService.queryByBusiness(
                queryBusinessUserPermissionDetails);
        Map<String, List<UserPermission>> usernameMap = userPermissions.stream()
                .collect(Collectors.groupingBy(UserPermission::getUsername));
//        Map<String, User> users = usernameMap.keySet()
//                .stream()
//                .map(userService::getByUsername)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toMap(User::getUsername, a -> a, (k1, k2) -> k1));
        return BusinessUserPermissionMerger.newMerger()
                .withUserPermissions(userPermissions)
                // .withUsers(users)
                .get();
    }

}
