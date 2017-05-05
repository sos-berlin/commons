package com.sos.exception;


public class DBConnectionException extends DBException {

    private static final long serialVersionUID = 1L;
    
    public DBConnectionException() {
        super();
    }

    public DBConnectionException(String message) {
        super(message);
    }
    
    public DBConnectionException(Throwable cause) {
        super(cause);
    }
    
    public DBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
