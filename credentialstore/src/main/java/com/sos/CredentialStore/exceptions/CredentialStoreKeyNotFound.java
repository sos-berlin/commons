/**
 * 
 */
package com.sos.CredentialStore.exceptions;

import com.sos.CredentialStore.Options.SOSCredentialStoreOptions;
import com.sos.JSHelper.Exceptions.JobSchedulerException;

/** @author KB */
public class CredentialStoreKeyNotFound extends SOSCredentialStoreException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -895707771551951127L;

    /**
	 * 
	 */
    public CredentialStoreKeyNotFound(final SOSCredentialStoreOptions pobjOptions) {
        super(String.format("SOSVfsCS_E_001: Entry '%1$s' in database '%2$s' not found", pobjOptions.credentialStoreKeyPath.Value(),
                pobjOptions.credentialStoreFileName.Value()));
        objCredentialStoreOptions = pobjOptions;
        gflgStackTracePrinted = true;
        this.Status(JobSchedulerException.FATAL);
    }

}
