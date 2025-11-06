package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.facade.UserExtFacade;
import com.baiyi.cratos.facade.UserFacade;
import com.baiyi.cratos.facade.inspection.base.BaseInspection;
import com.baiyi.cratos.facade.inspection.model.ResignedUserModel;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.RESIGNATION_USERS_INSPECTION_NOTIFICATION;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/21 11:31
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class ResignationUsersInspection extends BaseInspection {

    private final UserService userService;
    private final UserExtFacade userExtFacade;
    private final UserFacade userFacade;
    private final EdsIdentityFacade edsIdentityFacade;
    private final TagService tagService;

    public ResignationUsersInspection(NotificationTemplateService notificationTemplateService,
                                      DingtalkService dingtalkService, EdsInstanceHelper edsInstanceHelper,
                                      EdsConfigService edsConfigService, UserService userService,
                                      UserExtFacade userExtFacade, UserFacade userFacade,
                                      EdsIdentityFacade edsIdentityFacade, TagService tagService) {
        super(notificationTemplateService, dingtalkService, edsInstanceHelper, edsConfigService);
        this.userService = userService;
        this.userExtFacade = userExtFacade;
        this.userFacade = userFacade;
        this.edsIdentityFacade = edsIdentityFacade;
        this.tagService = tagService;
    }

    public final static String[] RESIGNATION_USER_TABLE_FIELD_NAME = {"Username", "Name", "DisplayName", "Email", "Phone"};
    private static final String USERS_FIELD = "users";
    private static final boolean PRINT_TABLE = true;

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(RESIGNATION_USERS_INSPECTION_NOTIFICATION);
        List<User> resignedUsers = queryResignedUsers();
        // debug
        if (PRINT_TABLE && !CollectionUtils.isEmpty(resignedUsers)) {
            printTable(resignedUsers);
        }
        List<ResignedUserModel.User> users = resignedUsers.stream()
                .map(e -> ResignedUserModel.User.builder()
                        .username(e.getUsername())
                        .name(StringUtils.hasText(e.getName()) ? e.getName() : "-")
                        .displayName(StringUtils.hasText(e.getDisplayName()) ? e.getDisplayName() : "-")
                        .email(StringUtils.hasText(e.getEmail()) ? e.getEmail() : "-")
                        .build())
                .toList();
        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put(USERS_FIELD, users)
                .build());
    }

    private void printTable(List<User> resignedUsers) {
        try {
            PrettyTable resignedUserTable = PrettyTable.fieldNames(RESIGNATION_USER_TABLE_FIELD_NAME);
            resignedUsers.forEach(
                    user -> resignedUserTable.addRow(user.getUsername(), user.getName(), user.getDisplayName(),
                            user.getEmail(), StringUtils.hasText(user.getMobilePhone()) ? user.getMobilePhone() : "-"));
            log.info("Resigned users: \n{}", resignedUserTable);
        } catch (Exception ignored) {
        }
    }

    private List<User> queryResignedUsers() {
        List<User> userList = userService.selectAll();
        if (CollectionUtils.isEmpty(userList)) {
            return List.of();
        }
        Map<String, UserVO.User> excludeUserMap = Maps.newHashMap();
        excludeUserMap.putAll(queryExtUserMap());
        excludeUserMap.putAll(queryUserTypeMap());
        return userList.stream()
                .filter(User::getValid)
                .filter(user -> !excludeUserMap.containsKey(user.getUsername()))
                .filter(user -> {
                    EdsIdentityParam.QueryDingtalkIdentityDetails queryDetails = EdsIdentityParam.QueryDingtalkIdentityDetails.builder()
                            .username(user.getUsername())
                            .build();
                    EdsIdentityVO.DingtalkIdentityDetails dingtalkDetails = edsIdentityFacade.queryDingtalkIdentityDetails(
                            queryDetails);
                    return CollectionUtils.isEmpty(dingtalkDetails.getDingtalkIdentities());
                })
                .collect(Collectors.toList());
    }

    private Map<String, UserVO.User> queryExtUserMap() {
        // 所有外部用户
        UserExtParam.UserExtPageQuery pageQuery = UserExtParam.UserExtPageQuery.builder()
                .page(1)
                .length(1024)
                .build();
        DataTable<UserVO.User> table = userExtFacade.queryExtUserPage(pageQuery);
        return table.getData()
                .stream()
                .collect(Collectors.toMap(UserVO.User::getUsername, user -> user));
    }

    private Map<String, UserVO.User> queryUserTypeMap() {
        Tag tag = tagService.getByTagKey(SysTagKeys.USER_TYPE);
        if (Objects.isNull(tag)) {
            return Map.of();
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .businessType(BusinessTypeEnum.USER.name())
                .build();
        UserParam.UserPageQuery pageQuery = UserParam.UserPageQuery.builder()
                .page(1)
                .length(1024)
                .queryByTag(queryByTag)
                .build();
        DataTable<UserVO.User> table = userFacade.queryUserPage(pageQuery);
        return table.getData()
                .stream()
                .collect(Collectors.toMap(UserVO.User::getUsername, user -> user));
    }

}
