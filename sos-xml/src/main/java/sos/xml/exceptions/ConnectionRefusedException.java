package sos.xml.exceptions;


public class ConnectionRefusedException extends Exception {

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
