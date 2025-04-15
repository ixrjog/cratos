package com.baiyi.cratos.model;

import com.baiyi.cratos.common.exception.CommandExecException;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.YamlDump;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.google.gson.JsonSyntaxException;
import lombok.*;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class CommandExecModel {

    public static ExecTarget loadAs(CommandExec commandExec) {
        if (Objects.isNull(commandExec) || !StringUtils.hasText(commandExec.getExecTargetContent())) {
            return ExecTarget.EMPTY;
        }
        try {
            return YamlUtils.loadAs(commandExec.getExecTargetContent(), ExecTarget.class);
        } catch (JsonSyntaxException e) {
            throw new CommandExecException("ExecTarget content format error: {}", e.getMessage());
        }
    }

    public static ExecTarget loadAs(CommandExecVO.CommandExec commandExec) {
        if (Objects.isNull(commandExec) || !StringUtils.hasText(commandExec.getExecTargetContent())) {
            return ExecTarget.EMPTY;
        }
        try {
            return YamlUtils.loadAs(commandExec.getExecTargetContent(), ExecTarget.class);
        } catch (JsonSyntaxException e) {
            throw new CommandExecException("ExecTarget content format error: {}", e.getMessage());
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecTarget extends YamlDump {
        public static final ExecTarget EMPTY = ExecTarget.builder()
                .build();
        private EdsInstance instance;
        private Boolean useDefaultExecContainer;
        private Long maxWaitingTime;

        public CommandExecVO.ExecTarget toVO() {
            return CommandExecVO.ExecTarget.builder()
                    .instance(CommandExecVO.EdsInstance.builder()
                            .id(this.instance.getId())
                            .name(this.instance.getName())
                            .namespace(this.instance.getNamespace())
                            .build())
                    .useDefaultExecContainer(this.useDefaultExecContainer)
                    .maxWaitingTime(this.maxWaitingTime)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EdsInstance {
        private String name;
        private Integer id;
        private String namespace;
    }

}