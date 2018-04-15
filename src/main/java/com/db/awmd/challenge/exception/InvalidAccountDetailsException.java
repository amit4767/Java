package com.db.awmd.challenge.exception;

public class InvalidAccountDetailsException extends RuntimeException {
    public InvalidAccountDetailsException(String errormessage){ super(errormessage);}
}
