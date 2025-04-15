package com.baiyi.cratos.domain.util;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.model.I18nModel;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 14:26
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class I18nUtils {

    public static I18nModel.I18nData loadAs(I18nModel.HasI18n hasI18n) throws JsonSyntaxException {
        if (!org.springframework.util.StringUtils.hasText(hasI18n.getI18n())) {
            return I18nModel.I18nData.NO_DATA;
        }
        return YamlUtils.loadAs(hasI18n.getI18n(), I18nModel.I18nData.class);
    }

    public static void setI18nData(I18nModel.HasI18n hasI18n) {
        I18nModel.I18nData data = loadAs(hasI18n);
        hasI18n.setI18nData(data);
    }

}
