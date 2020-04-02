package com.sos.credentialstore.options;

import com.sos.JSHelper.Options.SOSOptionBoolean;
import com.sos.JSHelper.Options.SOSOptionInFileName;
import com.sos.JSHelper.Options.SOSOptionOutFileName;
import com.sos.JSHelper.Options.SOSOptionPassword;
import com.sos.JSHelper.Options.SOSOptionString;

public interface ISOSCredentialStoreOptions {

    public abstract SOSOptionString getCredentialStoreAuthenticationMethod();

    public abstract void setCredentialStoreAuthenticationMethod(SOSOptionString pCredentialStoreAuthenticationMethod);

    public abstract SOSOptionBoolean getCredentialStoreDeleteExportedFileOnExit();

    public abstract void setCredentialStoreDeleteExportedFileOnExit(SOSOptionBoolean pCredentialStoreDeleteExportedFileOnExit);

    public abstract SOSOptionInFileName getCredentialStoreFileName();

    public abstract void setCredentialStoreFileName(SOSOptionInFileName pCredentialStoreFileName);

    public abstract SOSOptionInFileName getCredentialStoreKeyFileName();

    public abstract void setCredentialStoreKeyFileName(SOSOptionInFileName pCredentialStoreKeyFileName);

    public abstract SOSOptionString getCredentialStoreKeyPath();

    public abstract void setCredentialStoreKeyPath(SOSOptionString pCredentialStoreKeyPath);

    public abstract SOSOptionBoolean getCredentialStoreProcessNotesParams();

    public abstract void setCredentialStoreProcessNotesParams(SOSOptionBoolean pCredentialStoreProcessNotesParams);

    public abstract SOSOptionString getCredentialStoreStoreType();

    public abstract void setCredentialStoreStoreType(SOSOptionString pCredentialStoreStoreType);

    public abstract SOSOptionBoolean getCredentialStoreExportAttachment();

    public abstract void setCredentialStoreExportAttachment(SOSOptionBoolean pCredentialStoreExportAttachment);

    public abstract SOSOptionOutFileName getCredentialStoreExportAttachment2FileName();

    public abstract void setCredentialStoreExportAttachment2FileName(SOSOptionOutFileName pCredentialStoreExportAttachment2FileName);

    public abstract SOSOptionPassword getCredentialStorePassword();

    public abstract void setCredentialStorePassword(SOSOptionPassword pCredentialStorePassword);

    public abstract SOSOptionBoolean getUseCredentialStore();

    public abstract void setUseCredentialStore(SOSOptionBoolean pUseCredentialStore);
    
}