package com.baiyi.cratos.common.util;

import com.google.gson.JsonSyntaxException;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2023/3/29 13:45
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class YamlUtil {

    /**
     * 2.0
     *
     * @param loadYaml
     * @param targetClass
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T loadAs(String loadYaml, Class<T> targetClass) throws JsonSyntaxException {
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils()
                .setSkipMissingProperties(true);
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector(tag -> true);
        Constructor constructor = new Constructor(targetClass, loaderOptions);
        Yaml yaml = new Yaml(constructor, representer);
        return yaml.loadAs(loadYaml, targetClass);
    }

    public static <T> String dump(T o) throws JsonSyntaxException {
        DumperOptions dumperOptions = new DumperOptions();
        // 设置层级显示
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        // 显示开始结束分隔符
        dumperOptions.setExplicitStart(true);
        dumperOptions.setExplicitEnd(true);
        // 缩进
        dumperOptions.setIndent(2);
        Yaml yaml = new Yaml(dumperOptions);
        return yaml.dump(o);
    }

}