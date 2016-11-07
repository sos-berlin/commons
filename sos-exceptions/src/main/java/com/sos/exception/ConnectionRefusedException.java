package com.sos.exception;


public class ConnectionRefusedException extends SOSException {

    private static final long serialVersionUID = 1L;
    
    public ConnectionRefusedException() {
        super();
    }

    public ConnectionRefusedException(String message) {
        super(message);
    }
    
    public ConnectionRefusedException(Throwable cause) {
        super(cause);
    }
    
    public ConnectionRefusedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionRefusedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
