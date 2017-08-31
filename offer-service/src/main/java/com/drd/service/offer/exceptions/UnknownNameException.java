package com.drd.service.offer.exceptions;

import javax.ejb.ApplicationException;

/**
 * Author: drd 2017-08-28
 */
@ApplicationException()
public class UnknownNameException extends RuntimeException {

    public UnknownNameException(String message) {
        super(message);
    }
}
