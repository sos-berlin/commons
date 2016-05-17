package com.sos.CredentialStore.Options;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;

@JSOptionClass(name = "SOSCredentialStoreOptions", description = "SOSCredentialStore")
public class SOSCredentialStoreOptions extends SOSCredentialStoreOptionsSuperClass {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(SOSCredentialStoreOptions.class);

    public SOSCredentialStoreOptions() {
        LOGGER.trace("constructor SOSCredentialStoreOptions");
    }

    @Deprecated
    public SOSCredentialStoreOptions(final JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    }

    public SOSCredentialStoreOptions(final HashMap<String, String> jsSettings) throws Exception {
        super(jsSettings);
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