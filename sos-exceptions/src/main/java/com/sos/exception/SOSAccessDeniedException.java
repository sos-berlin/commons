package com.sos.exception;


public class SOSAccessDeniedException extends SOSException {

    private static final long serialVersionUID = 1L;
    
    public SOSAccessDeniedException() {
        super();
    }

    public SOSAccessDeniedException(String message) {
        super(message);
    }
    
    public SOSAccessDeniedException(Throwable cause) {
        super(cause);
    }
    
    public SOSAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SOSAccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
