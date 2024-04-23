package com.baiyi.cratos.shell.commands.custom;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.commands.AbstractCommand;
import com.baiyi.cratos.shell.commands.SshShellComponent;
import com.baiyi.cratos.shell.pagination.TableFooter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static com.baiyi.cratos.shell.commands.custom.HostAssetCommand.GROUP;

/**
 * @Author baiyi
 * @Date 2024/4/17 下午3:23
 * @Version 1.0
 */
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Host Asset Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class HostAssetCommand extends AbstractCommand {

    public static final String GROUP = "asset";
    private static final String COMMAND_ASSET_ECS_LIST = GROUP + "-ecs-list";
    private static final String COMMAND_ASSET_EC2_LIST = GROUP + "-ec2-list";

    private final EdsAssetService edsAssetService;

    private final EdsInstanceService edsInstanceService;

    private final UserService userService;

    public final static String[] ASSET_TABLE_FIELD_NAME = {"EDS Name", "Instance ID", "Region", "Type", "Name", "IP"};

    protected static final int PAGE_FOOTER_SIZE = 6;

    public HostAssetCommand(SshShellHelper helper, SshShellProperties properties, EdsInstanceService edsInstanceService, EdsAssetService edsAssetService, UserService userService) {
        super(helper, properties, properties.getCommands()
                .getAssetHost());
        this.edsInstanceService = edsInstanceService;
        this.edsAssetService = edsAssetService;
        this.userService = userService;
    }

    @ShellMethod(key = COMMAND_ASSET_ECS_LIST, value = "List host by ecs asset")
    @ShellMethodAvailability("ecsAssetListAvailability")
    public void ecsList(@ShellOption(help = "Name", defaultValue = "") String name, @ShellOption(help = "Asset Type", valueProvider = EnumValueProvider.class, defaultValue = ShellOption.NULL) String assetType, @ShellOption(help = "Page", defaultValue = "1") int page) {
        PrettyTable ecsTable = PrettyTable.fieldNames(ASSET_TABLE_FIELD_NAME);
        int rows = helper.terminalSize()
                .getRows();
        int pageLength = rows - PAGE_FOOTER_SIZE;
        EdsInstanceParam.AssetPageQuery pageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .assetType(EdsAssetTypeEnum.ALIYUN_ECS.name())
                .page(page)
                .length(pageLength)
                .queryName(name)
                .build();

        DataTable<EdsAsset> table = edsAssetService.queryEdsInstanceAssetPage(pageQuery);
        if (CollectionUtils.isEmpty(table.getData())) {
            helper.printInfo("No available assets found.");
        }
        Map<Integer, String> edsInstanceMap = Maps.newHashMap();

        for (EdsAsset asset : table.getData()) {
            if (!edsInstanceMap.containsKey(asset.getInstanceId())) {
                EdsInstance edsInstance = edsInstanceService.getById(asset.getInstanceId());
                edsInstanceMap.put(edsInstance.getId(), edsInstance.getInstanceName());
            }
            ecsTable.addRow(edsInstanceMap.getOrDefault(asset.getInstanceId(), "-"), asset.getAssetId(), asset.getRegion(), helper.getColored("ECS", PromptColor.GREEN), asset.getName(), asset.getAssetKey());
        }
        helper.print(ecsTable.toString());

        User user = userService.getByUsername(helper.getSshSession()
                .getUsername());
        TableFooter.Pagination pagination = TableFooter.Pagination.builder()
                .lang(user.getLang())
                .totalNum(table.getTotalNum())
                .page(page)
                .length(pageLength)
                .lang(user.getLang())
                .build();
        helper.print(pagination.toStr(), PromptColor.GREEN);
    }

}
