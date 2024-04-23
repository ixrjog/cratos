package com.baiyi.cratos.shell.commands.custom;

import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.commands.AbstractCommand;
import com.baiyi.cratos.shell.commands.SshShellComponent;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void hostList(@ShellOption(help = "Asset Name", defaultValue = "") String name,
                         @ShellOption(help = "Asset Type", valueProvider = EnumValueProvider.class, defaultValue = ShellOption.NULL) AssetTypeEnum assetType) {
        helper.print(assetType.name());

    }

    //    PrettyTable lbTable = PrettyTable.fieldNames(LB_TABLE_FIELD_NAME);
    //        Optional.of(originServer)
    //                .map(TrafficLayerRecordVO.OriginServer::getOrigins)
    //                .ifPresent(assets -> assets.forEach(e -> lbTable.addRow(e.getName(), e.getAssetKey(), e.getAssetType())));
    //        return lbTable.toString();

    public Set<String> getAssetTypes() {
        return Sets.newHashSet(EdsAssetTypeEnum.ALIYUN_ECS.name(), EdsAssetTypeEnum.AWS_EC2.name());
    }

    public enum AssetTypeEnum {
        ALIYUN_ECS, AWS_EC2
    }

}




@Slf4j
@Component
class AssetTypeValuesProvider implements ValueProvider {

    private final HostAssetCommand hostAssetCommand;

    AssetTypeValuesProvider(HostAssetCommand hostAssetCommand) {
        this.hostAssetCommand = hostAssetCommand;
    }

    @Override
    public List<CompletionProposal> complete(CompletionContext completionContext) {
        return hostAssetCommand.getAssetTypes().stream().map(CompletionProposal::new).collect(Collectors.toList());
    }
}
