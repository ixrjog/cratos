package com.baiyi.cratos.shell.command.custom.eds;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.SimpleEdsAccountFacade;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.context.ComputerAssetContext;
import com.baiyi.cratos.shell.pagination.TableFooter;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/4/17 下午3:23
 * @Version 1.0
 */
@Slf4j
//@Component
//@SshShellComponent
//@ShellCommandGroup("Eds Computer Commands")
//@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsCloudComputerListCommand extends AbstractCommand {

    public static final String GROUP = "computer";
    private static final String COMMAND_ALIYUN_ECS_ASSET_LIST = GROUP + "-aliyun-ecs-list";
    private static final String COMMAND_AWS_EC2_ASSET_LIST = GROUP + "-aws-ec2-list";

    private final EdsAssetService edsAssetService;
    private final EdsInstanceService edsInstanceService;
    private final UserService userService;
    private final SimpleEdsAccountFacade simpleEdsAccountFacade;

    public final static String[] ASSET_TABLE_FIELD_NAME = {"ID", "Instance ID", "Name", "IP", "EDS Name", "Region", "Type", "Login Account"};
    protected static final int PAGE_FOOTER_SIZE = 6;

    public EdsCloudComputerListCommand(SshShellHelper helper, SshShellProperties properties,
                                       EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                       UserService userService, SimpleEdsAccountFacade simpleEdsAccountFacade) {
        super(helper, properties, properties.getCommands()
                .getComputer());
        this.edsInstanceService = edsInstanceService;
        this.edsAssetService = edsAssetService;
        this.userService = userService;
        this.simpleEdsAccountFacade = simpleEdsAccountFacade;
    }

    private void doList(String name, int page, EdsAssetTypeEnum edsAssetTypeEnum) {
        PrettyTable computerTable = PrettyTable.fieldNames(ASSET_TABLE_FIELD_NAME);
        int rows = helper.terminalSize()
                .getRows();
        int pageLength = rows - PAGE_FOOTER_SIZE;
        EdsInstanceParam.AssetPageQuery pageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .assetType(edsAssetTypeEnum.name())
                .page(page)
                .length(pageLength)
                .queryName(name)
                .build();
        DataTable<EdsAsset> table = edsAssetService.queryEdsInstanceAssetPage(pageQuery);
        if (CollectionUtils.isEmpty(table.getData())) {
            helper.printInfo("No available assets found.");
        }
        Map<Integer, String> edsInstanceMap = Maps.newHashMap();
        Map<Integer, EdsAsset> computerMapper = Maps.newHashMap();
        List<ServerAccount> serverAccounts = simpleEdsAccountFacade.queryServerAccounts();
        int i = 1;
        for (EdsAsset asset : table.getData()) {
            if (!edsInstanceMap.containsKey(asset.getInstanceId())) {
                EdsInstance edsInstance = edsInstanceService.getById(asset.getInstanceId());
                edsInstanceMap.put(edsInstance.getId(), edsInstance.getInstanceName());
            }
            String edsName = edsInstanceMap.getOrDefault(asset.getInstanceId(), "-");
            String instanceId = asset.getAssetId();
            String region = asset.getRegion();
            String type = helper.getColored("ECS", PromptColor.GREEN);
            String serverName = asset.getName();
            String ip = asset.getAssetKey();
            computerTable.addRow(i, instanceId, serverName, ip, edsName, region, type,
                    toServerAccounts(serverAccounts));
            computerMapper.put(i, asset);
            i++;
        }
        // set thread context
        ComputerAssetContext.setContext(computerMapper, serverAccounts);
        // 打印表格
        helper.print(computerTable.toString());

        User user = userService.getByUsername(helper.getSshSession()
                .getUsername());
        TableFooter.Pagination pagination = TableFooter.Pagination.builder()
                .lang(user.getLang())
                .totalNum(table.getTotalNum())
                .page(page)
                .length(pageLength)
                .lang(user.getLang())
                .build();
        // 打印页脚/分页
        helper.print(pagination.toStr(), PromptColor.GREEN);
    }

    @ShellMethod(key = COMMAND_ALIYUN_ECS_ASSET_LIST, value = "List computer asset by aliyun ecs")
    public void aliyunEcsList(@ShellOption(help = "Name", defaultValue = "") String name,
                        @ShellOption(help = "Page", defaultValue = "1") int page) {
        this.doList(name, page, EdsAssetTypeEnum.ALIYUN_ECS);
    }

    @ShellMethod(key = COMMAND_AWS_EC2_ASSET_LIST, value = "List computer asset by aws ec2")
    public void awsEc2List(@ShellOption(help = "Name", defaultValue = "") String name,
                        @ShellOption(help = "Page", defaultValue = "1") int page) {
        this.doList(name, page, EdsAssetTypeEnum.AWS_EC2);
    }

    private String toServerAccounts(List<ServerAccount> serverAccounts) {
        if (CollectionUtils.isEmpty(serverAccounts)) {
            return "-";
        }
        return Joiner.on(" ")
                .join(serverAccounts.stream()
                        .map(this::toServerAccount)
                        .toList());
    }

    private String toServerAccount(ServerAccount serverAccount) {
        if (serverAccount.getSudo()) {
            return helper.getColored(serverAccount.getName(), PromptColor.RED);
        } else {
            return helper.getColored(serverAccount.getName(), PromptColor.GREEN);
        }
    }

}
