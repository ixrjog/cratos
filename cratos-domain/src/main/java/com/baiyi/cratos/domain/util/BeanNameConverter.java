package com.baiyi.cratos.domain.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2024/1/22 18:01
 * @Version 1.0
 */
public class BeanNameConverter {

    private BeanNameConverter() {
    }

    public static String serviceImplNameToMapperName(String serviceImplSimpleName) {
        return StringUtils.uncapitalize(serviceImplSimpleName.replace("ServiceImpl", "Mapper"));
    }

}
