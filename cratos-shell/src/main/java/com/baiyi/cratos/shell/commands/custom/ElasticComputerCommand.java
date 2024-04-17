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

import static com.baiyi.cratos.shell.commands.custom.ElasticComputerCommand.GROUP;

/**
 * @Author baiyi
 * @Date 2024/4/17 下午3:23
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Asset Host Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class ElasticComputerCommand extends AbstractCommand {

    public static final String GROUP = "asset-host";
    private static final String COMMAND_EC_LIST = GROUP + "-list";

    public ElasticComputerCommand(SshShellHelper helper, SshShellProperties properties) {
        super(helper, properties, properties.getCommands().getAssetHost());
    }

    /**
     * List jmx mbeans
     *
     * @param pattern (optional) allows you to narrow search
     */
    @ShellMethod(key = COMMAND_EC_LIST, value = "List ECS/EC2 mbeans.")
    @ShellMethodAvailability("computerListAvailability")
    public void jmxList(@ShellOption(help = "Pattern to search for (ex: org.springframework.boot:*, org.springframework.boot:type=Endpoint,name=*,)", defaultValue = ShellOption.NULL) String pattern) {

    }

}
