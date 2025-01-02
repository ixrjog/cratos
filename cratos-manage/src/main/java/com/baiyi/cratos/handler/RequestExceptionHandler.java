package com.baiyi.cratos.handler;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author baiyi
 * @Date 2024/1/3 09:42
 * @Version 1.0
 */
@RestControllerAdvice(basePackages = {"com.baiyi.cratos.controller.http"})
public class RequestExceptionHandler {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return new HttpResult<>(ErrorEnum.SYSTEM_ERROR.getCode(), exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
    }

}