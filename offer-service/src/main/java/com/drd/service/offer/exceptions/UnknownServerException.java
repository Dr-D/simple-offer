package com.drd.service.offer.exceptions;

/**
 * Author: drd 2017-08-28
 */
public class UnknownServerException extends RuntimeException {

    public UnknownServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownServerException(String message) {
        super(message);


    }
}
