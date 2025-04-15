package com.baiyi.cratos.eds.core.util;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsConfigException;
import com.google.gson.JsonSyntaxException;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/27 14:12
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class ConfigUtils {

    /**
     * https://stackabuse.com/reading-and-writing-yaml-files-in-java-with-snakeyaml/
     *
     * @param yaml
     * @param targetClass
     * @param <C>
     * @return
     */
    public static <C extends IEdsConfigModel> C loadAs(String yaml, Class<C> targetClass) {
        if (StringUtils.isEmpty(yaml)) {
            throw new EdsConfigException("The eds config file is empty.");
        }
        try {
            return YamlUtils.loadAs(yaml, targetClass);
        } catch (JsonSyntaxException e) {
            throw new EdsConfigException("Eds config file conversion error.");
        }
    }

}
