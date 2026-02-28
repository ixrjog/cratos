package com.baiyi.cratos.shell.command.custom.acme;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.service.acme.AcmeOrderService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.baiyi.cratos.shell.context.AcmeContext;
import com.baiyi.cratos.shell.writer.AcmeOrderTableWriter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.shredzone.acme4j.Status;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.baiyi.cratos.shell.command.custom.computer.EdsComputerListCommand.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/27 14:00
 * &#064;Version 1.0
 */
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds CloudComputer Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class AcmeOrderCommand extends AbstractCommand {

    public static final String GROUP = "acme-order";
    private static final String COMMAND_ACME_ORDER_LIST = GROUP + "-list";

    public static final String[] ACME_ORDER_TABLE_FIELD_NAME = {"ID", "Domain", "Domains", "Order URL", "Status", "Create Time", "Expires"};
    private final AcmeOrderService acmeOrderService;

    public AcmeOrderCommand(SshShellHelper helper, SshShellProperties properties, AcmeOrderService acmeOrderService) {
        super(
                helper, properties, properties.getCommands()
                        .getAcme()
        );
        this.acmeOrderService = acmeOrderService;
    }

    @ShellAuthentication(resource = "/acme")
    @ShellMethod(key = {COMMAND_ACME_ORDER_LIST}, value = "List ACME domain orders")
    public void listAcmeOrder(@ShellOption(help = "Domain ID", defaultValue = "") Integer domainId) {
        if (!AcmeContext.getDomainContext()
                .containsKey(domainId)) {
            helper.printInfo("Domain ID 为空.");
            return;
        }
        AcmeDomain acmeDomain = AcmeContext.getDomainContext()
                .get(domainId);
        int rows = helper.terminalSize()
                .getRows();
        PrettyTable acmeOrderTable = PrettyTable.fieldNames(ACME_ORDER_TABLE_FIELD_NAME);
        List<AcmeOrder> acmeOrders = acmeOrderService.queryByDomainId(acmeDomain.getId(), rows);
        if (CollectionUtils.isEmpty(acmeOrders)) {
            helper.printInfo("No available orders found.");
        }
        final Map<Integer, AcmeOrder> acmeOrderMapper = Maps.newHashMap();
        int id = 1;
        for (AcmeOrder acmeOrder : acmeOrders) {
            AcmeOrderTableWriter.newBuilder()
                    .withTable(acmeOrderTable)
                    .withId(id)
                    .withDomain(acmeDomain.getDomain())
                    .withDomains(acmeDomain.getDomains())
                    .withOrderUrl(acmeOrder.getOrderUrl())
                    .withStatus(toStatus(acmeOrder.getOrderStatus()))
                    .withCreateTime(TimeUtils.parse(acmeOrder.getCreateTime(), Global.ISO8601))
                    .withExpires(TimeUtils.parse(acmeOrder.getExpires(), Global.ISO8601))
                    .addRow();
            acmeOrderMapper.put(id, acmeOrder);
            id++;
        }
        // set thread context
        AcmeContext.setOrderContext(acmeOrderMapper);
        // 打印表格
        helper.print(acmeOrderTable.toString());
    }

    /**
     * 染色
     *
     * @param statusName
     * @return
     */
    private String toStatus(String statusName) {
        try {
            Status status = Status.valueOf(statusName);
            return switch (status) {
                case PENDING, PROCESSING -> SshShellHelper.getColoredMessage(statusName, PromptColor.YELLOW);
                case VALID -> SshShellHelper.getColoredMessage(statusName, PromptColor.GREEN);
                case INVALID -> SshShellHelper.getColoredMessage(statusName, PromptColor.RED);
                case READY -> SshShellHelper.getColoredMessage(statusName, PromptColor.CYAN);
                default -> statusName;
            };
        } catch (IllegalArgumentException e) {
            return statusName;
        }
    }

}