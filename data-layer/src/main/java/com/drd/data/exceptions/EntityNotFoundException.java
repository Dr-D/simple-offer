package com.drd.data.exceptions;

import javax.ejb.ApplicationException;

/**
 * Used to wrap no result exception to allow better handling higher in the stack
 *
 * Author: drd 2017-08-28
 */
@ApplicationException()
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
