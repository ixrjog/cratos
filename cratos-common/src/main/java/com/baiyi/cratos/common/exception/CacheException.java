package com.baiyi.cratos.common.exception;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/1/23 11:49
 * @Version 1.0
 */
public class CacheException extends BaseException {

    @Serial
    private static final long serialVersionUID = 632298599405869915L;

    public CacheException(String message) {
        super(message);
    }

}
