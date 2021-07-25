package week02;/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */

/**
 * @author wuwh
 * @version HttpProcessException.java, v 0.1 2021年06月29日 15:43 wuwh
 */
public class HttpProcessException extends RuntimeException {

    public HttpProcessException() {
        super();
    }

    public HttpProcessException(String message) {
        super(message);
    }

    public HttpProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpProcessException(Throwable cause) {
        super(cause);
    }

    protected HttpProcessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}