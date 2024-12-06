package com.baiyi.cratos.facade.application.builder.util;

import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.view.application.kubernetes.common.KubernetesCommonVO;
import io.fabric8.kubernetes.api.model.ObjectMeta;

import java.text.ParseException;
import java.util.Date;

import static com.baiyi.cratos.domain.constant.Global.ISO8601;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 16:22
 * &#064;Version 1.0
 */
public class ConverterUtil {

    public static KubernetesCommonVO.Metadata toMetadata(ObjectMeta objectMeta) {
        return KubernetesCommonVO.Metadata.builder()
                .name(objectMeta.getName())
                .creationTimestamp(ConverterUtil.parse(objectMeta.getCreationTimestamp()))
                .generateName(objectMeta.getGenerateName())
                .uid(objectMeta.getUid())
                .namespace(objectMeta.getNamespace())
                .build();
    }

    public static Date parse(String strDate) {
        try {
            return TimeUtil.strToDate(strDate, ISO8601);
        } catch (ParseException ex) {
            return null;
        }
    }

}
