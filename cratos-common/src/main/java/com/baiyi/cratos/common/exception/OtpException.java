package com.baiyi.cratos.common.exception;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/1/19 17:23
 * @Version 1.0
 */
public class OtpException {

    public static class DecodingException extends BaseException {
        @Serial
        private static final long serialVersionUID = -6967442817224513199L;
        public DecodingException(String message) {
            super(message);
        }
    }

}
