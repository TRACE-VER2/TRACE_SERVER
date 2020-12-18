package com.trace.traceproject.advice.exception;

public class InvalidAuthenticationTokenException extends RuntimeException{

    public InvalidAuthenticationTokenException() {
        super();
    }

    public InvalidAuthenticationTokenException(String message) {
        super(message);
    }

    public InvalidAuthenticationTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthenticationTokenException(Throwable cause) {
        super(cause);
    }
}
