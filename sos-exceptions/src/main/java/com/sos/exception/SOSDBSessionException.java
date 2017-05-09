package com.sos.exception;


public class SOSDBSessionException extends SOSDBException {

    private static final long serialVersionUID = 1L;
    
    public SOSDBSessionException() {
        super();
    }

    public SOSDBSessionException(String message) {
        super(message);
    }
    
    public SOSDBSessionException(Throwable cause) {
        super(cause);
    }
    
    public SOSDBSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SOSDBSessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
