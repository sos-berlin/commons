package com.sos.exception;


public class DBSessionException extends SOSException {

    private static final long serialVersionUID = 1L;
    
    public DBSessionException() {
        super();
    }

    public DBSessionException(String message) {
        super(message);
    }
    
    public DBSessionException(Throwable cause) {
        super(cause);
    }
    
    public DBSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBSessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
