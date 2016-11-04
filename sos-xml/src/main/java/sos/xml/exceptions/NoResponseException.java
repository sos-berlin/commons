package sos.xml.exceptions;


public class NoResponseException extends Exception {

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
