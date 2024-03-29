package com.sos.credentialstore.exceptions;

import com.sos.exception.SOSException;

public class SOSCredentialStoreException extends SOSException {

    private static final long serialVersionUID = 1L;

    public SOSCredentialStoreException(final String msg) {
        super(msg);
    }

    public SOSCredentialStoreException(final String msg, Throwable t) {
        super(msg, t);
    }

    public SOSCredentialStoreException(Throwable t) {
        super(t);
    }
}
