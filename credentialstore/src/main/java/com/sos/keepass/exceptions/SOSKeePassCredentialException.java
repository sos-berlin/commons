package com.sos.keepass.exceptions;

public class SOSKeePassCredentialException extends SOSKeePassDatabaseException {

    private static final long serialVersionUID = 1L;

    public SOSKeePassCredentialException(final String msg, Throwable t) {
        super(msg, t);
    }

    public SOSKeePassCredentialException(Throwable t) {
        super(t);
    }
}
