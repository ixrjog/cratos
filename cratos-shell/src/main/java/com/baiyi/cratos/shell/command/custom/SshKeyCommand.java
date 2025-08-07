package com.baiyi.cratos.shell.command.custom;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.SshFingerprintUtil;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.PromptColor;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.SshShellProperties;
import com.baiyi.cratos.shell.command.AbstractCommand;
import com.baiyi.cratos.shell.command.SshShellComponent;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.baiyi.cratos.shell.command.custom.SshKeyCommand.GROUP;

/**
 * @Author baiyi
 * @Date 2024/4/19 下午4:10
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("SSH Key Commands")
@ConditionalOnProperty(name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + GROUP + ".create", havingValue = "true", matchIfMissing = true)
public class SshKeyCommand extends AbstractCommand {

    private final UserService userService;
    private final BusinessCredentialService businessCredentialService;
    private final CredentialService credentialService;

    public static final String GROUP = "sshkey";
    private static final String COMMAND_SSHKEY_LIST = GROUP + "-list";
    private static final String COMMAND_SSHKEY_ADD = GROUP + "-add";

    public final static String[] SSH_KEY_TABLE_FIELD_NAME = {"Username", "Title", "Fingerprint", "Expired Time"};

    public SshKeyCommand(SshShellHelper helper, SshShellProperties properties,
                         UserService userService, BusinessCredentialService businessCredentialService,
                         CredentialService credentialService) {
        super(helper, properties, properties.getCommands().getSshkey());
        this.userService = userService;
        this.businessCredentialService = businessCredentialService;
        this.credentialService = credentialService;
    }

    @ShellMethod(key = COMMAND_SSHKEY_LIST, value = "List my sshkey")
    @ShellMethodAvailability("sshKeyListAvailability")
    public void listSshKey() {
        final String username = helper.getSshSession()
                .getUsername();
        User user = userService.getByUsername(username);
        if (user == null) {
            return;
        }
        SimpleBusiness query = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        List<BusinessCredential> businessCredentials = businessCredentialService.selectByBusiness(query);
        if (!CollectionUtils.isEmpty(businessCredentials)) {
            List<Credential> credentials = Lists.newArrayList();

            for (BusinessCredential businessCredential : businessCredentials) {
                Credential cred = credentialService.getById(businessCredential.getCredentialId());
                if (cred != null && cred.getPrivateCredential() && cred.getValid() && CredentialTypeEnum.SSH_USERNAME_WITH_PUBLIC_KEY.name()
                        .equals(cred.getCredentialType())) {
                    credentials.add(cred);
                }
            }

            if (CollectionUtils.isEmpty(credentials)) {
                noAvailableKeys();
                return;
            }

            PrettyTable table = PrettyTable.fieldNames(SSH_KEY_TABLE_FIELD_NAME);
            credentials.forEach(e -> table.addRow(e.getUsername(), e.getTitle(), e.getFingerprint(),
                    getExpiredTime(e.getExpiredTime())));
            helper.print(table.toString());
            return;
        }
        noAvailableKeys();
    }

    @ShellMethod(key = COMMAND_SSHKEY_ADD, value = "Add my sshkey")
    @ShellMethodAvailability("sshKeyAddAvailability")
    public void addSshKey(
            @ShellOption(help = "Specify the public key and wrap it in single quotes (ex: 'ssh-rsa AAAAB3NzaC1yc...')", defaultValue = "") String pubKey) {
        final String username = helper.getSshSession()
                .getUsername();
        User user = userService.getByUsername(username);
        if (user == null) {
            return;
        }
        Credential credential = Credential.builder()
                .title(getTitle(pubKey))
                .username(username)
                .credentialType(CredentialTypeEnum.SSH_USERNAME_WITH_PUBLIC_KEY.name())
                .credential(pubKey)
                .fingerprint(SshFingerprintUtil.calcFingerprint(null, pubKey))
                .privateCredential(true)
                .valid(true)
                .expiredTime(ExpiredUtil.generateExpirationTime(366L * 5, TimeUnit.DAYS))
                .build();
        credentialService.add(credential);

        BusinessCredential businessCredential = BusinessCredential.builder()
                .credentialId(credential.getId())
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        businessCredentialService.add(businessCredential);
    }

    private String getTitle(String pubKey) {
        String[] ss = pubKey.split(" ");
        if (ss.length <= 2) {
            return "";
        }
        return ss[2];
    }

    private String getExpiredTime(Date expiredTime) {
        if (expiredTime == null) {
            helper.getSuccess("Never expire");
        }

        String time = TimeUtils.parse(expiredTime, Global.ISO8601);
        return ExpiredUtil.isExpired(expiredTime) ? helper.getError(time) : helper.getSuccess(time);
    }

    private void noAvailableKeys() {
        helper.print("No available sshkeys.", PromptColor.GREEN);
    }

}