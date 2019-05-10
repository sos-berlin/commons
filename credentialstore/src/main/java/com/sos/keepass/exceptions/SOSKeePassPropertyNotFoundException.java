package com.sos.keepass.exceptions;

public class SOSKeePassPropertyNotFoundException extends SOSKeePassDatabaseException {

    private static final long serialVersionUID = 1L;

    public SOSKeePassPropertyNotFoundException(final String msg) {
        super(msg);
    }
}
