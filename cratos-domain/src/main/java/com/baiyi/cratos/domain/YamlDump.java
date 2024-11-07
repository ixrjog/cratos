package com.baiyi.cratos.domain;

import com.google.gson.JsonSyntaxException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/6 10:48
 * &#064;Version 1.0
 */
public class YamlDump {

    public String dump() throws JsonSyntaxException {
        DumperOptions dumperOptions = new DumperOptions();
        // 设置层级显示
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        // 显示开始结束分隔符
        dumperOptions.setExplicitStart(true);
        dumperOptions.setExplicitEnd(true);
        // 缩进
        dumperOptions.setIndent(2);
        Yaml yaml = new Yaml(dumperOptions);
        return yaml.dump(this);
    }

}
