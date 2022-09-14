package com.marsh.sqlapi.exception;


import com.marsh.zutils.exception.BaseErrorCode;

public class ErrorCode {

    public static final BaseErrorCode ERROR_CAPTCHA_CODE = new BaseErrorCode("000001", "ERROR_CAPTCHA_CODE");

    public static final BaseErrorCode USER_NOT_EXIST = new BaseErrorCode("000002", "USER_NOT_EXIST");
    public static final BaseErrorCode USER_IS_EXIST = new BaseErrorCode("000003", "USER_IS_EXIST");
    public static final BaseErrorCode ERROR_PASSWORD = new BaseErrorCode("000004", "ERROR_PASSWORD");
    public static final BaseErrorCode DSDataNotFound = new BaseErrorCode("100001", "DS_DATA_NOT_FOUND");



}
