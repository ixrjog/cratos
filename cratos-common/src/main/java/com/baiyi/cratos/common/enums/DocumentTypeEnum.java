package com.baiyi.cratos.common.enums;

import com.baiyi.cratos.common.exception.BusinessException;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2024/2/19 14:01
 * @Version 1.0
 */
public enum DocumentTypeEnum {

    MARKDOWN,
    HTML,
    TEXT;

    public static void verifyValueOf(String documentType) {
        try {
            DocumentTypeEnum.valueOf(documentType);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Unsupported document type! please enter: {}", Joiner.on(",")
                    .join(DocumentTypeEnum.values()));
        }
    }

}
