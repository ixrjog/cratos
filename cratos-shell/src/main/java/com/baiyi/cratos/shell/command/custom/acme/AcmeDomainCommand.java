package com.baiyi.cratos.shell.command.custom.acme;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.acme.AcmeDomainParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.acme.AcmeAccountService;
import com.baiyi.cratos.service.acme.AcmeDomainService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.baiyi.cratos.shell.context.AcmeContext;
import com.baiyi.cratos.shell.pagination.TableFooter;
import com.baiyi.cratos.shell.writer.AcmeDomainTableWriter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.shell.command.custom.computer.EdsComputerListCommand.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/27 10:47
 * &#064;Version 1.0
 */
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds CloudComputer Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class AcmeDomainCommand extends AbstractCommand {

    public static final String GROUP = "acme-domain";
    private static final String COMMAND_ACME_DOMAIN_LIST = GROUP + "-list";

    public static final String[] ACME_DOMAIN_TABLE_FIELD_NAME = {"ID", "Name", "Domain", "Domains", "DNS Resolver Instance", "Account and ACME Provider" , "DCV Delegation"};
    protected static final int PAGE_FOOTER_SIZE = 6;
    private final AcmeDomainService acmeDomainService;
    private final EdsInstanceService edsInstanceService;
    private final AcmeAccountService acmeAccountService;
    private final UserService userService;

    public AcmeDomainCommand(SshShellHelper helper, SshShellProperties properties, AcmeDomainService acmeDomainService,
                             EdsInstanceService edsInstanceService, AcmeAccountService acmeAccountService,
                             UserService userService) {
        super(
                helper, properties, properties.getCommands()
                        .getAcme()
        );
        this.acmeDomainService = acmeDomainService;
        this.edsInstanceService = edsInstanceService;
        this.acmeAccountService = acmeAccountService;
        this.userService = userService;
    }

    @ShellAuthentication(resource = "/acme")
    @ShellMethod(key = {COMMAND_ACME_DOMAIN_LIST}, value = "List ACME domain")
    public void listAcmeDomain(@ShellOption(help = "Domain", defaultValue = "") String domain,
                               @ShellOption(help = "Page", defaultValue = "1") int page) {
        User user = userService.getByUsername(helper.getSshSession()
                                                      .getUsername());
        int rows = helper.terminalSize()
                .getRows();
        PrettyTable acmeDomainTable = PrettyTable.fieldNames(ACME_DOMAIN_TABLE_FIELD_NAME);
        int pageLength = rows - PAGE_FOOTER_SIZE;
        AcmeDomainParam.DomainPageQuery pageQuery = AcmeDomainParam.DomainPageQuery.builder()
                .queryName(domain)
                .page(page)
                .length(pageLength)
                .build();
        DataTable<AcmeDomain> dataTable = acmeDomainService.queryAcmeDomainPage(pageQuery);

        if (CollectionUtils.isEmpty(dataTable.getData())) {
            helper.printInfo("No available domains found.");
        }
        final Map<Integer, String> dnsResolverInstanceMap = Maps.newHashMap();
        final Map<Integer, String> acmeAccountMap = Maps.newHashMap();
        final Map<Integer, AcmeDomain> acmeDomainMapper = Maps.newHashMap();
        int id = 1;
        for (AcmeDomain acmeDomain : dataTable.getData()) {
            String dnsResolverInstance = getDnsResolverInstance(
                    dnsResolverInstanceMap,
                    acmeDomain.getDnsResolverInstanceId()
            );
            String acmeAccount = getAcmeAccount(acmeAccountMap, acmeDomain.getAccountId());
            AcmeDomainTableWriter.newBuilder()
                    .withTable(acmeDomainTable)
                    .withId(id)
                    .withDomain(acmeDomain.getDomain())
                    .withDomains(acmeDomain.getDomains())
                    .withName(acmeDomain.getName())
                    .withAccount(acmeAccount)
                    .withDnsResolverInstance(dnsResolverInstance)
                    .withDcvDelegation(Optional.ofNullable(acmeDomain.getDcvType())
                                               .orElse(Global.NONE))
                    .addRow();
            acmeDomainMapper.put(id, acmeDomain);
            id++;
        }
        // set thread context
        AcmeContext.setDomainContext(acmeDomainMapper);
        // 打印表格
        helper.print(acmeDomainTable.toString());
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

    private String getDnsResolverInstance(Map<Integer, String> dnsResolverInstanceMap, Integer dnsResolverInstanceId) {
        if (!dnsResolverInstanceMap.containsKey(dnsResolverInstanceId)) {
            EdsInstance edsInstance = edsInstanceService.getById(dnsResolverInstanceId);
            dnsResolverInstanceMap.put(edsInstance.getId(), edsInstance.getInstanceName());
        }
        return dnsResolverInstanceMap.get(dnsResolverInstanceId);
    }

    private String getAcmeAccount(Map<Integer, String> acmeAccountMap, Integer acmeAccountId) {
        if (!acmeAccountMap.containsKey(acmeAccountId)) {
            AcmeAccount acmeAccount = acmeAccountService.getById(acmeAccountId);
            String accountName = StringFormatter.arrayFormat(
                    "{}({})|{}", acmeAccount.getName(), acmeAccount.getEmail(), acmeAccount.getAcmeProvider());
            acmeAccountMap.put(acmeAccountId, accountName);
        }
        return acmeAccountMap.get(acmeAccountId);
    }

}
