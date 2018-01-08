package com.sos.CredentialStore.exceptions;

import java.util.Date;

public class CredentialStoreEntryExpired extends SOSCredentialStoreException {

    private static final long serialVersionUID = 8190764135924155901L;

    public CredentialStoreEntryExpired(final Date date) {
        super(String.format(String.format("SOSVfsCS_E_001: Entry is expired, valid until %1$s. Processing aborted", date)));
        gflgStackTracePrinted = true;
        this.setStatus(FATAL);
    }
}
