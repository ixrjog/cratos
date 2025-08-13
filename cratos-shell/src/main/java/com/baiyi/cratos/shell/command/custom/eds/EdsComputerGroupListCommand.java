package com.baiyi.cratos.shell.command.custom.eds;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.facade.UserPermissionFacade;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.shell.command.custom.eds.EdsComputerGroupListCommand.GROUP;

/**
 * @Author 修远
 * @Date 2025/6/9 14:30
 * @Since 1.0
 */
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds Computer Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsComputerGroupListCommand extends AbstractCommand {

    public static final String GROUP = "computer";
    private static final String COMMAND_GROUP_LIST = GROUP + "-group-list";

    private final UserPermissionFacade permissionFacade;
    private final UserService userService;
    public final static String[] GROUP_TABLE_FIELD_NAME = {"ID", "GROUP NAME"};
    private final EnvFacade envFacade;
    public static final String UNAUTHORIZED = "--";
    public final String AUTHORIZATION_EXPIRED;

    public EdsComputerGroupListCommand(SshShellHelper helper, SshShellProperties properties, UserService userService,
                                       UserPermissionFacade permissionFacade, EnvFacade envFacade) {
        super(helper, properties, properties.getCommands()
                .getComputer());
        this.userService = userService;
        this.permissionFacade = permissionFacade;
        this.envFacade = envFacade;
        this.AUTHORIZATION_EXPIRED = helper.getColored("Expired", PromptColor.YELLOW);
    }

    @ShellMethod(key = COMMAND_GROUP_LIST, value = "List computer group")
    public void listComputerGroup() {
        User user = userService.getByUsername(helper.getSshSession()
                .getUsername());
        UserPermissionParam.QueryAllBusinessUserPermissionDetails queryAllBusinessUserPermissionDetails = UserPermissionParam.QueryAllBusinessUserPermissionDetails.builder()
                .username(user.getUsername())
                .businessType(BusinessTypeEnum.TAG_GROUP.name())
                .build();
        UserPermissionVO.UserPermissionDetails userPermissionDetails = permissionFacade.queryUserPermissionDetails(
                queryAllBusinessUserPermissionDetails);
        if (CollectionUtils.isEmpty(userPermissionDetails.getUserPermissions())) {
            helper.printInfo("No authorized groups");
            return;
        }
        List<String> fieldNames = new ArrayList<>(Arrays.asList(GROUP_TABLE_FIELD_NAME));
        List<Env> envs = envFacade.querySorted();
        envs.forEach(env -> fieldNames.add(StringUtils.capitalize(env.getEnvName())));
        PrettyTable computerGroupTable = PrettyTable.fieldNames(fieldNames.toArray(new String[0]));
        int id = 1;
        for (UserPermissionVO.UserPermissionBusiness userPermission : userPermissionDetails.getUserPermissions()) {
            Object[] rowData = new Object[fieldNames.size()];
            rowData[0] = id++;
            rowData[1] = userPermission.getName();
            for (int i = 0; i < userPermission.getUserPermissions()
                    .size(); i++) {
                rowData[2 + i] = toPermission(userPermission.getUserPermissions()
                        .get(i));
            }
            computerGroupTable.addRow(rowData);
        }
        helper.print(computerGroupTable.toString());
    }

    private String toPermission(UserPermissionVO.Permission permission) {
        if (Objects.isNull(permission) || permission.getExpiredTime() == null) {
            return UNAUTHORIZED;
        }
        if (ExpiredUtils.isExpired(permission.getExpiredTime())) {
            return AUTHORIZATION_EXPIRED;
        }
        return helper.getColored(TimeUtils.parse(permission.getExpiredTime(), Global.ISO8601), PromptColor.GREEN);
    }

}