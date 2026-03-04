package com.baiyi.cratos.shell.command.custom.acme;

import com.aliyun.cas20200407.models.ListCloudResourcesResponseBody;
import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.eds.acme.deploy.impl.AcmeAliyunCertDeployer;
import com.baiyi.cratos.service.acme.AcmeCertificateService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.baiyi.cratos.shell.context.AcmeContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.cratos.shell.command.custom.computer.EdsComputerListCommand.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/27 14:56
 * &#064;Version 1.0
 */
@Slf4j
@Component
@SshShellComponent
@ShellCommandGroup("Eds CloudComputer Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class AcmeCertCommand extends AbstractCommand {

    public static final String GROUP = "acme-cert";
    private static final String COMMAND_ACME_CERT_SHOW = GROUP + "-show";
    private static final String COMMAND_ACME_CERT_ISSUE = GROUP + "-issue";
    private static final String COMMAND_ACME_CERT_CLOUD_RESOURCE_LIST = GROUP + "-cloud-resources-list";

    public static final String[] ACME_CERT_TABLE_FIELD_NAME = {"Domains", "Not Before", "Not After"};
    public static final String[] CERT_CLOUD_RESOURCE_TABLE_FIELD_NAME = {"ID", "Cert Name", "Cert ID", "Cloud Name", "Cloud Product", "Resource ID"};

    private final AcmeCertificateService acmeCertificateService;
    private final com.baiyi.cratos.domain.facade.AcmeFacade acmeFacade;
    private final AcmeAliyunCertDeployer acmeAliyunCertDeployer;

    public AcmeCertCommand(SshShellHelper helper, SshShellProperties properties,
                           AcmeCertificateService acmeCertificateService,
                           com.baiyi.cratos.domain.facade.AcmeFacade acmeFacade,
                           AcmeAliyunCertDeployer acmeAliyunCertDeployer) {
        super(
                helper, properties, properties.getCommands()
                        .getAcme()
        );
        this.acmeCertificateService = acmeCertificateService;
        this.acmeFacade = acmeFacade;
        this.acmeAliyunCertDeployer = acmeAliyunCertDeployer;
    }

    @ShellAuthentication(resource = "/acme")
    @ShellMethod(key = {COMMAND_ACME_CERT_SHOW}, value = "Show ACME order certificate")
    public void showAcmeCert(@ShellOption(help = "Order ID", defaultValue = "") Integer orderId) {
        if (!AcmeContext.getOrderContext()
                .containsKey(orderId)) {
            helper.printInfo("Order ID 为空.");
            return;
        }
        AcmeOrder acmeOrder = AcmeContext.getOrderContext()
                .get(orderId);
        if (!IdentityUtils.hasIdentity(acmeOrder.getCertificateId())) {
            helper.printInfo("当前订单没有颁发证书.");
            return;
        }
        AcmeCertificate acmeCertificate = acmeCertificateService.getById(acmeOrder.getCertificateId());
        PrettyTable acmeCertTable = PrettyTable.fieldNames(ACME_CERT_TABLE_FIELD_NAME);
        acmeCertTable.addRow(
                acmeCertificate.getDomains(), TimeUtils.parse(acmeCertificate.getNotBefore(), Global.ISO8601),
                TimeUtils.parse(acmeCertificate.getNotAfter(), Global.ISO8601)
        );
        helper.print(acmeCertTable.toString());
        // 现在打印证书信息
        helper.print("Certificate:", PromptColor.GREEN);
        helper.printInfo(acmeCertificate.getCertificate());
        helper.print("Certificate Chain:", PromptColor.GREEN);
        helper.printInfo(acmeCertificate.getCertificateChain());
        helper.print("Private Key(Confidential information, do not disclose):", PromptColor.RED);
        helper.printInfo(acmeCertificate.getPrivateKey());
    }

    @ShellAuthentication(resource = "/acme")
    @ShellMethod(key = {COMMAND_ACME_CERT_ISSUE}, value = "Show ACME order certificate")
    public void issueAcmeCert(@ShellOption(help = "Domain ID", defaultValue = "") Integer domainId) {
        if (!AcmeContext.getDomainContext()
                .containsKey(domainId)) {
            helper.printInfo("Domain ID 为空.");
            return;
        }
        AcmeDomain acmeDomain = AcmeContext.getDomainContext()
                .get(domainId);
        acmeFacade.asyncIssueCertificate(acmeDomain.getId());
        helper.print(
                "Issuing the certificate will take some time, please check the order in a few minutes.",
                PromptColor.GREEN
        );
    }

    @ShellAuthentication(resource = "/acme")
    @ShellMethod(key = {COMMAND_ACME_CERT_CLOUD_RESOURCE_LIST}, value = "List ACME certificate deployment cloud resources")
    public void listCloudResources(@ShellOption(help = "Deployment ID", defaultValue = "") Integer deploymentId,
                                   @ShellOption(help = "Cert Name", defaultValue = "") String certName) {
        List<ListCloudResourcesResponseBody.ListCloudResourcesResponseBodyData> resources = acmeAliyunCertDeployer.listCloudResourcesByCertName(
                deploymentId, certName);
        if (CollectionUtils.isEmpty(resources)) {
            return;
        }
        PrettyTable cloudResourceTable = PrettyTable.fieldNames(CERT_CLOUD_RESOURCE_TABLE_FIELD_NAME);
        int id = 1;
        for (ListCloudResourcesResponseBody.ListCloudResourcesResponseBodyData resource : resources) {
            cloudResourceTable.addRow(
                    id, resource.getCertName(), resource.getCertId(), resource.getCloudName(),
                    resource.getCloudProduct(), resource.getId()
            );
            id++;
        }
        helper.print(cloudResourceTable.toString());
    }

}
