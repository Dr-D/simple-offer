package com.drd.rest.model;

/**
 * Author: drd 2017-08-25
 */
public class StructuredError {
    private int status;
    private String message;

    public StructuredError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
