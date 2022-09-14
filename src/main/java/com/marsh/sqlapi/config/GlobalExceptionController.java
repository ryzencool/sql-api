package com.marsh.sqlapi.config;

import com.marsh.zutils.entity.BaseResponse;
import com.marsh.zutils.exception.BaseBizException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(value = {BaseBizException.class})
    public BaseResponse<Void> handleException(BaseBizException ex) {
        return BaseResponse.fail(ex.getCode(),ex.getMessage());
    }
}