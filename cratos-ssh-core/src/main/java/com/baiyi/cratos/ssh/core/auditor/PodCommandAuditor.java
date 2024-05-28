package com.baiyi.cratos.ssh.core.auditor;

import com.baiyi.cratos.service.SshSessionInstanceCommandService;
import com.baiyi.cratos.service.SshSessionInstanceService;
import com.baiyi.cratos.ssh.core.auditor.base.BaseCommandAuditor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/28 下午3:36
 * &#064;Version 1.0
 */
@Component
public class PodCommandAuditor extends BaseCommandAuditor {

    // fix 匹配\u001b 0次或1次
    private static final String INPUT_REGEX = ".*# \\u001b?.*";

    // ".?\b\\u001b\\[J"
    private static final String BS_REGEX = ".*# \\u001b?[\\[J]?";

    public PodCommandAuditor(SshSessionInstanceService sshSessionInstanceService,
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

    @Override
    protected String eraseInvisibleCharacters(String input) {
        String in = super.eraseInvisibleCharacters(input);
        in = in.replaceAll("\\[6n","");
        return in.replaceAll("\\[J","");
    }

}
