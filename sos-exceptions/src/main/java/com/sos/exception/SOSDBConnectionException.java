package com.sos.exception;


@Deprecated
public class SOSDBConnectionException extends SOSDBException {

    private static final long serialVersionUID = 1L;
    
    public SOSDBConnectionException() {
        super();
    }

    public SOSDBConnectionException(String message) {
        super(message);
    }
    
    public SOSDBConnectionException(Throwable cause) {
        super(cause);
    }
    
    public SOSDBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SOSDBConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
