package com.sos.CredentialStore.Options;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;

@JSOptionClass(name = "SOSCredentialStoreOptions", description = "SOSCredentialStore")
public class SOSCredentialStoreOptions extends SOSCredentialStoreOptionsSuperClass {

    private static final long serialVersionUID = 1L;

    public SOSCredentialStoreOptions() {
    }

    public SOSCredentialStoreOptions(final HashMap<String, String> settings) throws Exception {
        super(settings);
    }

    @Override
    public void checkMandatory() {
        try {
            super.checkMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    }

}