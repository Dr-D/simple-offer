package com.drd.service.offer.exceptions;

import javax.ejb.ApplicationException;

/**
 * Author: drd 2017-08-28
 */
@ApplicationException(rollback = true)
public class DuplicateNameException extends RuntimeException {

    public DuplicateNameException(String message) {
        super(message);
    }
}
