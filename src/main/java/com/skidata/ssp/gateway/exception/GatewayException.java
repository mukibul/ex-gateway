package com.skidata.ssp.gateway.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author firoz
 * @since 05/12/16
 */
public class GatewayException extends RuntimeException implements Serializable{
    private String message;

    private HttpStatus httpStatus;

    public GatewayException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
