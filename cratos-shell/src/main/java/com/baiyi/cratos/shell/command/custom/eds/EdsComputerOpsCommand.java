package com.baiyi.cratos.shell.command.custom.eds;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.computer.CloudComputerOperatorFactory;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.annotation.ClearScreen;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.baiyi.cratos.shell.context.ComputerAssetContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static com.baiyi.cratos.shell.command.custom.eds.EdsComputerLoginCommand.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/19 14:47
 * &#064;Version 1.0
 */
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds CloudComputer Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsComputerOpsCommand extends AbstractCommand {

    public static final String GROUP = "computer";
    private static final String COMMAND_COMPUTER_OPS = GROUP + "-ops";

    @Value("${cratos.notification:NORMAL}")
    private String notification;
    @Value("${cratos.language:en-us}")
    protected String language;

    public EdsComputerOpsCommand(SshShellHelper helper, SshShellProperties properties) {
        super(
                helper, properties, properties.getCommands()
                        .getComputer()
        );
    }

    @ClearScreen
    @ShellMethod(key = {COMMAND_COMPUTER_OPS, "cops"}, value = "Ops computer.")
    @ShellAuthentication(resource = "/computer/ops")
    public void computerOps(@ShellOption(help = "ID", defaultValue = "1") int id,
                            @ShellOption(help = "Stop the computer", defaultValue = "false") boolean stop,
                            @ShellOption(help = "Start the computer", defaultValue = "false") boolean start,
                            @ShellOption(help = "Reboot the computer", defaultValue = "false") boolean reboot) {
        Map<Integer, EdsAsset> computerMapper = ComputerAssetContext.getComputerContext();
        if (CollectionUtils.isEmpty(computerMapper) || !computerMapper.containsKey(id)) {
            helper.print("CloudComputer does not exist, exec computer-list first, then ops", PromptColor.RED);
            return;
        }
        EdsAsset asset = computerMapper.get(id);
        String username = helper.getSshSession()
                .getUsername();
        log.info(
                "Computer ops command: username={}, assetId={}, assetName={}, stop={}, start={}, reboot={}", username,
                asset.getId(), asset.getName(), stop, start, reboot
        );
        if (stop) {
            CloudComputerOperatorFactory.reboot(asset.getAssetType(), asset.getId());
            return;
        }
        if (start) {
            CloudComputerOperatorFactory.start(asset.getAssetType(), asset.getId());
            return;
        }
        if (reboot) {
            CloudComputerOperatorFactory.reboot(asset.getAssetType(), asset.getId());
            return;
        }
        helper.print("You must specify at least one of --stop, --start, or --reboot", PromptColor.RED);
    }

}