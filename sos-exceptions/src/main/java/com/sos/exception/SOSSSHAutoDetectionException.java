package com.sos.exception;


public class SOSSSHAutoDetectionException extends SOSException {

    private static final long serialVersionUID = 1L;
    
    public SOSSSHAutoDetectionException() {
        super();
    }

    public SOSSSHAutoDetectionException(String message) {
        super(message);
    }
    
    public SOSSSHAutoDetectionException(Throwable cause) {
        super(cause);
    }
    
    public SOSSSHAutoDetectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SOSSSHAutoDetectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
