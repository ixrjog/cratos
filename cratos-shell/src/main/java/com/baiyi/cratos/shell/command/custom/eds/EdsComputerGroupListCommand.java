package com.baiyi.cratos.shell.command.custom.eds;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.service.UserPermissionService;
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

import java.util.List;

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
    private final String UNAUTHORIZED;

    private final UserPermissionService userPermissionService;
    private final UserService userService;
    public final static String[] ASSET_TABLE_FIELD_NAME = {"ID", "GROUP NAME"};

    public EdsComputerGroupListCommand(SshShellHelper helper, SshShellProperties properties,
                                       UserPermissionService userPermissionService,
                                       UserService userService) {
        super(helper, properties, properties.getCommands()
                .getComputer());
        this.userPermissionService = userPermissionService;
        this.userService = userService;
        this.UNAUTHORIZED = helper.getColored("Unauthorized", PromptColor.RED);
    }

    @ShellMethod(key = COMMAND_GROUP_LIST, value = "List computer group")
    public void listComputerGroup() {
        PrettyTable computerTable = PrettyTable.fieldNames(ASSET_TABLE_FIELD_NAME);
        User user = userService.getByUsername(helper.getSshSession().getUsername());
        List<String> groups = userPermissionService.queryUserPermissionGroups(user.getUsername());

        if (CollectionUtils.isEmpty(groups)) {
            helper.printInfo("No available assets found.");
        }

        int id = 1;
        for (String group : groups) {
            computerTable.addRow(id++, group);
        }

        // 打印表格
        helper.print(computerTable.toString());
    }

}