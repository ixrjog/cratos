package com.baiyi.cratos.shell.command.custom.eds;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.facade.UserPermissionBusinessFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.query.EdsAssetQuery;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserPermissionService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.baiyi.cratos.shell.context.ComputerAssetContext;
import com.baiyi.cratos.shell.pagination.TableFooter;
import com.baiyi.cratos.shell.writer.ComputerTableWriter;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.shell.command.custom.eds.EdsCloudComputerListCommand.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/7 15:00
 * &#064;Version 1.0
 */
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds Computer Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class EdsComputerListCommand extends AbstractCommand {

    public static final String GROUP = "computer";
    private static final String COMMAND_LIST = GROUP + "-list";
    public final String UNAUTHORIZED;

    private final UserPermissionBusinessFacade userPermissionBusinessFacade;
    private final UserPermissionService userPermissionService;
    private final EdsInstanceService edsInstanceService;
    private final UserService userService;
    private final EnvFacade envFacade;
    private final BusinessTagFacade businessTagFacade;
    public static final String[] ASSET_TABLE_FIELD_NAME = {"ID", "Cloud", "Instance ID", "Type", "Region", "Group", "Env", "Name", "IP", "Login Account", "Permission"};
    protected static final int PAGE_FOOTER_SIZE = 6;

    public EdsComputerListCommand(SshShellHelper helper, SshShellProperties properties,
                                  EdsInstanceService edsInstanceService,
                                  UserPermissionBusinessFacade userPermissionBusinessFacade,
                                  UserPermissionService userPermissionService, BusinessTagFacade businessTagFacade,
                                  UserService userService, EnvFacade envFacade) {
        super(helper, properties, properties.getCommands()
                .getComputer());
        this.userPermissionBusinessFacade = userPermissionBusinessFacade;
        this.userPermissionService = userPermissionService;
        this.edsInstanceService = edsInstanceService;
        this.businessTagFacade = businessTagFacade;
        this.envFacade = envFacade;
        this.userService = userService;
        this.UNAUTHORIZED = helper.getColored("Unauthorized", PromptColor.RED);
    }

    @ShellMethod(key = COMMAND_LIST, value = "List computer asset")
    public void listComputer(@ShellOption(help = "Name", defaultValue = "") String name,
                             @ShellOption(help = "Group", defaultValue = "") String group,
                             @ShellOption(help = "Page", defaultValue = "1") int page) {
        PrettyTable computerTable = PrettyTable.fieldNames(ASSET_TABLE_FIELD_NAME);
        int rows = helper.terminalSize()
                .getRows();
        int pageLength = rows - PAGE_FOOTER_SIZE;
        User user = userService.getByUsername(helper.getSshSession()
                .getUsername());
        EdsAssetQuery.UserPermissionPageQueryParam queryParam = EdsAssetQuery.UserPermissionPageQueryParam.builder()
                .username(user.getUsername())
                .page(page)
                .length(pageLength)
                .queryName(name)
                .queryGroupName(group)
                .build();
        DataTable<EdsAsset> dataTable = userPermissionBusinessFacade.queryUserPermissionAssets(queryParam);
        if (CollectionUtils.isEmpty(dataTable.getData())) {
            helper.printInfo("No available assets found.");
        }
        final Map<Integer, String> edsInstanceMap = Maps.newHashMap();
        final Map<Integer, EdsAsset> computerMapper = Maps.newHashMap();
        List<ServerAccount> serverAccounts = userPermissionBusinessFacade.queryUserPermissionServerAccounts(
                user.getUsername());
        final Map<String, Env> envMap = envFacade.getEnvMap();
        int id = 1;
        for (EdsAsset asset : dataTable.getData()) {
            if (!edsInstanceMap.containsKey(asset.getInstanceId())) {
                EdsInstance edsInstance = edsInstanceService.getById(asset.getInstanceId());
                edsInstanceMap.put(edsInstance.getId(), edsInstance.getInstanceName());
            }
            final String env = getTagValue(asset, SysTagKeys.ENV);
            final String tagGroup = getTagValue(asset, SysTagKeys.GROUP);
            BusinessTag serverAccountBusinessTag = getBusinessTag(asset, SysTagKeys.SERVER_ACCOUNT);
            ComputerTableWriter.newBuilder()
                    .withTable(computerTable)
                    .withId(id)
                    .withAsset(asset)
                    .withCloud(edsInstanceMap.getOrDefault(asset.getInstanceId(), "-"))
                    .withGroup(tagGroup)
                    .withEnv(renderEnv(envMap, env))
                    .withServerAccounts(toServerAccounts(serverAccountBusinessTag, serverAccounts))
                    .withPermission(toPermission(user, tagGroup, env))
                    .withServerName(getServerName(asset))
                    .addRow();
            computerMapper.put(id, asset);
            id++;
        }
        // set thread context
        ComputerAssetContext.setContext(computerMapper, serverAccounts);
        // 打印表格
        helper.print(computerTable.toString());
        TableFooter.Pagination pagination = TableFooter.Pagination.builder()
                .lang(user.getLang())
                .totalNum(dataTable.getTotalNum())
                .page(page)
                .length(pageLength)
                .lang(user.getLang())
                .build();
        // 打印页脚/分页
        helper.print(pagination.toStr(), PromptColor.GREEN);
    }

    private String toPermission(User user, String group, String env) {
        UserPermission userPermission = userPermissionService.getUserPermissionTagGroup(user.getUsername(), group, env);
        if (Objects.isNull(userPermission)) {
            return UNAUTHORIZED;
        }
        if (ExpiredUtil.isExpired(userPermission.getExpiredTime())) {
            return UNAUTHORIZED;
        }
        return helper.getColored(StringFormatter.arrayFormat("Authorized `expires {}`",
                TimeUtils.parse(userPermission.getExpiredTime(), Global.ISO8601)), PromptColor.GREEN);
    }

    private String renderEnv(Map<String, Env> envMap, String env) {
        return envMap.containsKey(env) ? helper.getColored(env, PromptColor.valueOf(envMap.get(env)
                .getPromptColor())) : env;
    }

    private String getTagValue(EdsAsset edsAsset, SysTagKeys sysTagKey) {
        BusinessTag businessTag = getBusinessTag(edsAsset, sysTagKey);
        return Objects.nonNull(businessTag) ? businessTag.getTagValue() : "-";
    }

    private BusinessTag getBusinessTag(EdsAsset edsAsset, SysTagKeys sysTagKey) {
        return businessTagFacade.getBusinessTag(toHasBusiness(edsAsset), sysTagKey.getKey());
    }

    private String getServerName(EdsAsset edsAsset) {
        return Optional.ofNullable(businessTagFacade.getBusinessTag(toHasBusiness(edsAsset), SysTagKeys.NAME.getKey()))
                .map(BusinessTag::getTagValue)
                .orElse(edsAsset.getName());
    }

    private BaseBusiness.HasBusiness toHasBusiness(EdsAsset edsAsset) {
        return SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(edsAsset.getId())
                .build();
    }

    private String toServerAccounts(BusinessTag serverAccountBusinessTag, List<ServerAccount> serverAccounts) {
        if (CollectionUtils.isEmpty(serverAccounts)) {
            return "-";
        }
        if (Objects.nonNull(serverAccountBusinessTag)) {
            Optional<ServerAccount> optionalServerAccount = serverAccounts.stream()
                    .filter(e -> e.getUsername()
                            .equals(serverAccountBusinessTag.getTagValue()))
                    .findFirst();
            if (optionalServerAccount.isPresent()) {
                return toServerAccount(optionalServerAccount.get());
            }
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
