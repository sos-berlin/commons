package com.sos.keepass.exceptions;

import com.sos.credentialstore.exceptions.SOSCredentialStoreException;

public class SOSKeePassDatabaseException extends SOSCredentialStoreException {

    private static final long serialVersionUID = 1L;

    public SOSKeePassDatabaseException(final String msg) {
        super(msg);
    }

    public SOSKeePassDatabaseException(final String msg, Throwable t) {
        super(msg, t);
    }

    public SOSKeePassDatabaseException(Throwable t) {
        super(t);
    }
}
