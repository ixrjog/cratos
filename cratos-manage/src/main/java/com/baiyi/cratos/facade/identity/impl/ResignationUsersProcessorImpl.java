package com.baiyi.cratos.facade.identity.impl;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.facade.UserExtFacade;
import com.baiyi.cratos.facade.identity.ResignationUsersProcessor;
import com.baiyi.cratos.service.UserService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/21 11:31
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResignationUsersProcessorImpl implements ResignationUsersProcessor {

    private final UserService userService;
    private final UserExtFacade userExtFacade;
    private final EdsIdentityFacade edsIdentityFacade;

    public final static String[] RESIGNATION_USER_TABLE_FIELD_NAME = {"Username", "Name", "DisplayName", "Email", "Phone"};

    @Override
    public void doTask() {
        // 所有用户
        List<User> userList = userService.selectAll();
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        Map<String, UserVO.User> extUserMap = queryExtUserMap();
        PrettyTable resignationUsersTable = PrettyTable.fieldNames(RESIGNATION_USER_TABLE_FIELD_NAME);
        List<User> resignationUsers = Lists.newArrayList();
        userList.forEach(user -> {
            if (!user.getValid()) {
                return;
            }
            // 外部用户
            if (extUserMap.containsKey(user.getUsername())) {
                return;
            }
            EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails = EdsIdentityParam.QueryDingtalkIdentityDetails.builder()
                    .username(user.getUsername())
                    .build();
            EdsIdentityVO.DingtalkIdentityDetails dingtalkIdentityDetails = edsIdentityFacade.queryDingtalkIdentityDetails(
                    queryDingtalkIdentityDetails);
            if (CollectionUtils.isEmpty(dingtalkIdentityDetails.getDingtalkIdentities())) {
                resignationUsersTable.addRow(user.getUsername(), user.getName(), user.getDisplayName(), user.getEmail(),
                        StringUtils.hasText(user.getMobilePhone()) ? user.getMobilePhone() : "-");
                log.info("Resignation users table add: {}", user.getUsername());
                resignationUsers.add(user);
            }
        });
        if (!CollectionUtils.isEmpty(resignationUsers)) {
            log.info("Resignation users : \n{}", resignationUsersTable);
            // 处理离职用户
        }
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

}
