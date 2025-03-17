package com.baiyi.cratos.eds.opscloud;

import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.rbac.RbacUserRoleParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsOpscloudConfigModel;
import com.baiyi.cratos.eds.opscloud.model.OcApplicationVO;
import com.baiyi.cratos.eds.opscloud.model.OcUserVO;
import com.baiyi.cratos.eds.opscloud.repo.OcUserPermissionRepo;
import com.baiyi.cratos.eds.opscloud.repo.OcUserRepo;
import com.baiyi.cratos.facade.rbac.RbacUserRoleFacade;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.facade.UserPermissionBusinessFacade;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/12 14:04
 * &#064;Version 1.0
 */
public class EdsOpscloudUserTest extends BaseEdsTest<EdsOpscloudConfigModel.Opscloud> {

    @Resource
    private UserService userService;
    @Resource
    private ApplicationService applicationService;
    @Resource
    private BusinessTagFacade businessTagFacade;
    @Resource
    private UserPermissionBusinessFacade permissionBusinessFacade;
    @Resource
    private RbacUserRoleFacade rbacUserRoleFacade;

    @Test
    void importOcUserTest() {
        EdsOpscloudConfigModel.Opscloud cfg = getConfig(32);
        List<OcUserVO.User> ocUsers = OcUserRepo.listUser(cfg);
        if (CollectionUtils.isEmpty(ocUsers)) {
            return;
        }
        for (OcUserVO.User ocUser : ocUsers) {
            User user = userService.getByUsername(ocUser.getUsername());
            if (Objects.isNull(user)) {
                user = User.builder()
                        .name(ocUser.getName())
                        .username(ocUser.getUsername())
                        .displayName(ocUser.getDisplayName())
                        .mobilePhone(ocUser.getPhone())
                        .email(ocUser.getEmail())
                        .uuid(ocUser.getUuid())
                        .valid(ocUser.getIsActive())
                        .locked(!ocUser.getIsActive())
                        .build();
                userService.add(user);
            }

            // 查询用户的授权对象
            List<OcApplicationVO.Application> permissionApps = OcUserPermissionRepo.queryUserApplicationPermission(cfg,
                    ocUser.getId());
            if (CollectionUtils.isEmpty(permissionApps)) {
                continue;
            }

            List<UserPermissionBusinessParam.BusinessPermission> businessPermissions = Lists.newArrayList();
            for (OcApplicationVO.Application permissionApp : permissionApps) {
                Application application = applicationService.getByName(permissionApp.getName());
                List<UserPermissionBusinessParam.RoleMember> roleMembers = Lists.newArrayList();
                roleMembers.add(buildRoleMember("dev", true));
                roleMembers.add(buildRoleMember("daily", true));
                roleMembers.add(buildRoleMember("sit", true));

//                boolean checked = "admin".equalsIgnoreCase(permissionApp.getUserPermission()
//                        .getPermissionRole());
//                roleMembers.add(buildRoleMember("pre", checked));
//                roleMembers.add(buildRoleMember("prod", checked));

                UserPermissionBusinessParam.BusinessPermission businessPermission = UserPermissionBusinessParam.BusinessPermission.builder()
                        .businessId(application.getId())
                        .name(application.getName())
                        .roleMembers(roleMembers)
                        .build();
                businessPermissions.add(businessPermission);
                System.out.println("user: "+user.getUsername() + " app: " + application.getName());
            }
            UserPermissionBusinessParam.UpdateUserPermissionBusiness updateUserPermissionBusiness = UserPermissionBusinessParam.UpdateUserPermissionBusiness.builder()
                    .businessType(BusinessTypeEnum.APPLICATION.name())
                    .username(user.getUsername())
                    .businessPermissions(businessPermissions)
                    .build();

            permissionBusinessFacade.updateUserPermissionBusiness(updateUserPermissionBusiness);
        }
    }

    private UserPermissionBusinessParam.RoleMember buildRoleMember(String role, boolean checked) {
        return UserPermissionBusinessParam.RoleMember.builder()
                .role(role)
                .checked(checked)
                .expiredTime(checked ? ExpiredUtil.generateExpirationTime(90, TimeUnit.DAYS) : null)
                .build();
    }

    @Test
    void test() {
        EdsOpscloudConfigModel.Opscloud cfg = getConfig(32);
        List<OcApplicationVO.Application> permissionApps = OcUserPermissionRepo.queryUserApplicationPermission(cfg, 1);
        System.out.println(permissionApps.size());
    }

    @Test
    void roleTest() {
        List<User> users = userService.selectAll();
        for (User user : users) {
            if (!user.getValid() || user.getLocked()) {
                continue;
            }
            if (user.getUsername()
                    .startsWith("ext-")) {
                continue;
            }
            RbacUserRoleParam.AddUserRole addUserBaseRole = RbacUserRoleParam.AddUserRole.builder()
                    .username(user.getUsername())
                    .roleId(1)
                    .build();

            RbacUserRoleParam.AddUserRole addUserDevRole = RbacUserRoleParam.AddUserRole.builder()
                    .username(user.getUsername())
                    .roleId(5)
                    .build();
            rbacUserRoleFacade.addUserRole(addUserBaseRole);
            rbacUserRoleFacade.addUserRole(addUserDevRole);
            System.out.println(user.getUsername());
        }

    }


}
