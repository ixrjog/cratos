package com.baiyi.cratos.ssh.core.auditor;

import com.baiyi.cratos.domain.generator.SshSessionInstanceCommand;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/24 下午4:44
 * &#064;Version 1.0
 */
public class InstanceCommandBuilder {

    private final List<String> outputs;
    private final SshSessionInstanceCommand command;

    private InstanceCommandBuilder(SshSessionInstanceCommand command) {
        this.outputs = Lists.newArrayList();
        this.command = command;
    }

    static public InstanceCommandBuilder newBuilder(SshSessionInstanceCommand command) {
        return new InstanceCommandBuilder(command);
    }

    public InstanceCommandBuilder addOutput(String output) {
        if (outputs.size() < 10) {
            outputs.add(output);
        }
        return this;
    }

    public SshSessionInstanceCommand build() {
        command.setOutput(Joiner.on("\n").join(outputs));
        return command;
    }

}
