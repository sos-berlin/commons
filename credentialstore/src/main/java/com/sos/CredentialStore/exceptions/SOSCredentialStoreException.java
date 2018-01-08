package com.sos.CredentialStore.exceptions;

import com.sos.JSHelper.Exceptions.JobSchedulerException;

public class SOSCredentialStoreException extends JobSchedulerException {

    private static final long serialVersionUID = -6421417813962650491L;

    public SOSCredentialStoreException(final String msg) {
        super(msg);
    }
}
