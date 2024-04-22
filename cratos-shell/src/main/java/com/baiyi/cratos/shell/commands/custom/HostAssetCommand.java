package com.baiyi.cratos.shell.commands.custom;

import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.commands.AbstractCommand;
import com.baiyi.cratos.shell.commands.SshShellComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import static com.baiyi.cratos.shell.commands.custom.HostAssetCommand.GROUP;

/**
 * @Author baiyi
 * @Date 2024/4/17 下午3:23
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Host Asset Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class HostAssetCommand extends AbstractCommand {

    public static final String GROUP = "host-asset";
    private static final String COMMAND_ASSET_HOST_LIST = GROUP + "-list";

    public HostAssetCommand(SshShellHelper helper, SshShellProperties properties) {
        super(helper, properties, properties.getCommands().getAssetHost());
    }

    /**
     * List host asset
     * @param pattern
     */
    @ShellMethod(key = COMMAND_ASSET_HOST_LIST, value = "List host asset")
    @ShellMethodAvailability("hostListAvailability")
    public void hostList(@ShellOption(help = "Pattern to search for (ex: org.springframework.boot:*, org.springframework.boot:type=Endpoint,name=*,)", defaultValue = ShellOption.NULL) String pattern,
                         @ShellOption(help = "Asset Name", defaultValue = "") String name) {

    }

    //    PrettyTable lbTable = PrettyTable.fieldNames(LB_TABLE_FIELD_NAME);
    //        Optional.of(originServer)
    //                .map(TrafficLayerRecordVO.OriginServer::getOrigins)
    //                .ifPresent(assets -> assets.forEach(e -> lbTable.addRow(e.getName(), e.getAssetKey(), e.getAssetType())));
    //        return lbTable.toString();

}
