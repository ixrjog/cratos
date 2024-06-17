package com.baiyi.cratos.eds.core.util;

import com.baiyi.cratos.common.util.YamlUtil;
import com.baiyi.cratos.eds.core.exception.EdsAssetException;
import com.google.gson.JsonSyntaxException;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2021/6/18 5:07 下午
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class AssetUtil {

    public static boolean equals(String var1, String var2) {
        if (StringUtils.isEmpty(var1)) {
            return StringUtils.isEmpty(var2);
        }
        if (StringUtils.isEmpty(var2)) {
            return false;
        }
        return var1.equals(var2);
    }

    public static boolean equals(Date date1, Date data2) {
        if (date1 == null) {
            return data2 == null;
        }
        return date1.equals(data2);
    }

    public static <A> A loadAs(String yaml, Class<A> targetClass) {
        if (StringUtils.isEmpty(yaml)) {
            throw new EdsAssetException("The eds asset original model is empty.");
        }
        try {
            return YamlUtil.loadAs(yaml, targetClass);
        } catch (JsonSyntaxException e) {
            throw new EdsAssetException("Eds asset original model conversion error.");
        }
    }

}