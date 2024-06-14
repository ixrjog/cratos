package com.baiyi.cratos.domain.util;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/22 18:01
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class BeanNameConverter {

    public static String serviceImplNameToMapperName(String serviceImplSimpleName) {
        return StringUtils.uncapitalize(serviceImplSimpleName.replace("ServiceImpl", "Mapper"));
    }

}
