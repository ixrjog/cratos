package com.baiyi.cratos.ssh.core.auditor.base;

import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.generator.SshSessionInstanceCommand;
import com.baiyi.cratos.service.SshSessionInstanceCommandService;
import com.baiyi.cratos.service.SshSessionInstanceService;
import com.baiyi.cratos.ssh.core.auditor.InstanceCommandBuilder;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.regex.Pattern;

/**
 * &#064;Author  baiyi
 * &#064;Date  2021/7/28 4:31 下午
 * &#064;Version  1.0
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseCommandAuditor {

    private final SshSessionInstanceService sshSessionInstanceService;
    private final SshSessionInstanceCommandService sshSessionInstanceCommandService;

    protected abstract String getInputRegex();
    protected abstract String getBsRegex();

    /**
     * 记录命令 inpunt & output
     *
     * @param sessionId
     * @param instanceId
     */
    public void asyncRecordCommand(String sessionId, String instanceId) {
        SshSessionInstance sshSessionInstance = sshSessionInstanceService.getByUniqueKey(SshSessionInstance.builder()
                //.sessionId(sessionId)
                .instanceId(instanceId)
                .build());
        if (sshSessionInstance == null) {
            return;
        }
        // 跳过日志审计
        if (SshSessionInstanceTypeEnum.CONTAINER_LOG == SshSessionInstanceTypeEnum.valueOf(
                sshSessionInstance.getInstanceType())) {
            return;
        }

        final String auditPath = sshSessionInstance.getAuditPath();
        String str;
        InstanceCommandBuilder builder = null;
        String regex = getInputRegex();
        File file = new File(auditPath);
        if (!file.exists()) {
            log.debug("Ssh instance audit file {} does not exist. sessionId={}, instanceId={}", auditPath, sessionId,
                    instanceId);
            return;
        }
        // FIXME 多行输入
        try (LineNumberReader reader = new LineNumberReader(new FileReader(auditPath))) {
            while ((str = reader.readLine()) != null) {
                if (str.isEmpty()) {
                    continue;
                }
                boolean isInput = Pattern.matches(regex, str);
                if (isInput) {
                    if (builder != null) {
                        // save
                        SshSessionInstanceCommand auditCommand = builder.build();
                        if (auditCommand != null) {
                            if (!StringUtils.isEmpty(auditCommand.getInputFormatted())) {
                                sshSessionInstanceCommandService.add(auditCommand);
                            }
                            builder = null;
                        }
                    }
                    builder = builder(sshSessionInstance.getId(), str);
                } else {
                    if (builder != null) {
                        builder.addOutput(str);
                    }
                }
            }
        } catch (IOException ex) {
            log.error("Ssh instance audit write error: {}", ex.getMessage());
        }
    }

    /**
     * 同步接口
     *
     * @param instance
     */
    public void recordCommand(SshSessionInstance instance) {
        try {
            this.asyncRecordCommand(instance.getSessionId(), instance.getInstanceId());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    private ImmutablePair<Integer, Integer> getIndex(String inputStr) {
        int index1 = inputStr.indexOf("$");
        int index2 = inputStr.indexOf("#");
        if (index1 >= index2) {
            return ImmutablePair.of(index2, index1);
        }
        return ImmutablePair.of(index1, index2);
    }

    public InstanceCommandBuilder builder(Integer sshSessionInstanceId, String inputStr) {
        ImmutablePair<Integer, Integer> ip = getIndex(inputStr);
        int index = ip.getLeft() != -1 ? ip.getLeft() : ip.getRight();
        if (index == -1) {
            return null;
        }
        SshSessionInstanceCommand command = SshSessionInstanceCommand.builder()
                .sshSessionInstanceId(sshSessionInstanceId)
                .prompt(inputStr.substring(0, index + 1))
                // 取用户输入
                .input(inputStr.length() > index + 2 ? inputStr.substring(index + 2) : "")
                .build();
        formatInput(command);
        return InstanceCommandBuilder.newBuilder(command);
    }

    public void formatInput(SshSessionInstanceCommand command) {
        String input = command.getInput();
        while (input.contains("\b")) {
            // 退格处理
            String ni = input.replaceFirst(getBsRegex(), "");
            // 避免死循环
            if (ni.equals(input)) {
                ni = input.replaceFirst(".?\b", "");
            }
            input = ni;
        }
        String inputFormatted = eraseInvisibleCharacters(input);
        command.setInputFormatted(inputFormatted);
        command.setIsFormatted(true);
    }

    /**
     * 删除所有不可见字符（但不包含退格）
     *
     * @param input
     * @return
     */
    protected String eraseInvisibleCharacters(String input) {
        return input.replaceAll("\\p{C}", "");
    }

}