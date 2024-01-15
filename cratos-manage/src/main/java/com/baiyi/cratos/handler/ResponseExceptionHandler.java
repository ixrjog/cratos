package com.baiyi.cratos.handler;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.domain.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author baiyi
 * @Date 2024/1/3 09:46
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public HttpResult<?> handleRuntimeException(BaseException exception) {
        return new HttpResult<>(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(value = {EncryptionOperationNotPossibleException.class})
    public HttpResult<?> handleRuntimeException(EncryptionOperationNotPossibleException exception) {
        return new HttpResult<>(ErrorEnum.ENCRYPTION_AND_DECRYPTION_ERROR);
    }

    // 认证
    @ExceptionHandler(value = {BadCredentialsException.class})
    public HttpResult<?> handleRuntimeException(BadCredentialsException exception) {
        return new HttpResult<>(ErrorEnum.AUTHENTICATION_FAILED.getCode(), exception.getMessage());
    }

}