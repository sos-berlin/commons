package com.sos.exception;


public class NoResponseException extends SOSException {

    private static final long serialVersionUID = 1L;
    
    public NoResponseException() {
        super();
    }

    public NoResponseException(String message) {
        super(message);
    }
    
    public NoResponseException(Throwable cause) {
        super(cause);
    }
    
    public NoResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
