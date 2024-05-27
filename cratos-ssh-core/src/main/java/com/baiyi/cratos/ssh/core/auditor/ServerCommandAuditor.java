package com.baiyi.cratos.ssh.core.auditor;

import com.baiyi.cratos.service.SshSessionInstanceCommandService;
import com.baiyi.cratos.service.SshSessionInstanceService;
import com.baiyi.cratos.ssh.core.auditor.base.BaseCommandAuditor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/24 下午4:49
 * &#064;Version 1.0
 */
@Component
public class ServerCommandAuditor extends BaseCommandAuditor {

    private static final String INPUT_REGEX = ".*\\][$|#].*";

    private static final String BS_REGEX = ".?\b\\u001b\\[J";

    public ServerCommandAuditor(SshSessionInstanceService sshSessionInstanceService,
                                SshSessionInstanceCommandService sshSessionInstanceCommandService) {
        super(sshSessionInstanceService, sshSessionInstanceCommandService);
    }

    @Override
    protected String getInputRegex() {
        return INPUT_REGEX;
    }

    @Override
    protected String getBsRegex() {
        return BS_REGEX;
    }

}
