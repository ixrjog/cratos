package com.baiyi.cratos.facade.application.builder.util;

import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.view.application.kubernetes.common.KubernetesCommonVO;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.baiyi.cratos.domain.constant.Global.ISO8601;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 16:22
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConverterUtils {

    public static KubernetesCommonVO.Metadata toMetadata(ObjectMeta objectMeta) {
        return KubernetesCommonVO.Metadata.builder()
                .name(objectMeta.getName())
                .creationTimestamp(ConverterUtils.parse(objectMeta.getCreationTimestamp()))
                .generateName(objectMeta.getGenerateName())
                .uid(objectMeta.getUid())
                .namespace(objectMeta.getNamespace())
                .labels(objectMeta.getLabels())
                .build();
    }

    /**
     *
     * @param strDate
     * @return
     */
    public static Date parse(String strDate) {
        try {
            return TimeUtils.strToDate(strDate, ISO8601);
        } catch (Exception ex) {
            // ParseException
            // NullPointerException
            return null;
        }
    }

}
