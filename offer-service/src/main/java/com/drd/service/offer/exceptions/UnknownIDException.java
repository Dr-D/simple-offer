package com.drd.service.offer.exceptions;

import javax.ejb.ApplicationException;

/**
 * Author: drd 2017-08-28
 */
@ApplicationException()
public class UnknownIDException extends RuntimeException {

    public UnknownIDException(String message) {
        super(message);
    }
}
