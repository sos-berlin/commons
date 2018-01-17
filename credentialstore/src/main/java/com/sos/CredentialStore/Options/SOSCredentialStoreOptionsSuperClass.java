package com.sos.CredentialStore.Options;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import com.sos.JSHelper.Options.JSOptionsClass;
import com.sos.JSHelper.Options.SOSOptionBoolean;
import com.sos.JSHelper.Options.SOSOptionInFileName;
import com.sos.JSHelper.Options.SOSOptionOutFileName;
import com.sos.JSHelper.Options.SOSOptionPassword;
import com.sos.JSHelper.Options.SOSOptionString;

@JSOptionClass(name = "SOSCredentialStoreOptionsSuperClass", description = "SOSCredentialStoreOptionsSuperClass")
public class SOSCredentialStoreOptionsSuperClass extends JSOptionsClass {

    private static final long serialVersionUID = -4388209310315139254L;
    private final String className = this.getClass().getSimpleName();

    @JSOptionDefinition(name = "CredentialStore_AuthenticationMethod", description = "", key = "CredentialStore_AuthenticationMethod", type = "SOSOptionString", mandatory = true)
    public SOSOptionString credentialStoreAuthenticationMethod = new SOSOptionString(this, className + ".CredentialStore_AuthenticationMethod", "",
            "privatekey", "privatekey", true);
    public SOSOptionString csAuthenticationMethod = (SOSOptionString) credentialStoreAuthenticationMethod.setAlias(className
            + ".CS_AuthenticationMethod");

    public SOSOptionString getCredentialStoreAuthenticationMethod() {
        return credentialStoreAuthenticationMethod;
    }

    public void setCredentialStoreAuthenticationMethod(final SOSOptionString val) {
        credentialStoreAuthenticationMethod = val;
    }

    @JSOptionDefinition(name = "CredentialStore_DeleteExportedFileOnExit", description = "", key = "CredentialStore_DeleteExportedFileOnExit", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean credentialStoreDeleteExportedFileOnExit = new SOSOptionBoolean(this, className
            + ".CredentialStore_DeleteExportedFileOnExit", "", "true", "true", false);
    public SOSOptionBoolean csDeleteExportedFileOnExit = (SOSOptionBoolean) credentialStoreDeleteExportedFileOnExit.setAlias(className
            + ".CS_DeleteExportedFileOnExit");

    public SOSOptionBoolean getCredentialStoreDeleteExportedFileOnExit() {
        return credentialStoreDeleteExportedFileOnExit;
    }

    public void setCredentialStoreDeleteExportedFileOnExit(final SOSOptionBoolean val) {
        credentialStoreDeleteExportedFileOnExit = val;
    }

    @JSOptionDefinition(name = "CredentialStore_ExportAttachment", description = "", key = "CredentialStore_ExportAttachment", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean credentialStoreExportAttachment = new SOSOptionBoolean(this, className + ".CredentialStore_ExportAttachment", "", "false",
            "false", false);
    public SOSOptionBoolean csExportAttachment = (SOSOptionBoolean) credentialStoreExportAttachment.setAlias(className + ".CS_ExportAttachment");

    public SOSOptionBoolean getCredentialStoreExportAttachment() {
        return credentialStoreExportAttachment;
    }

    public void setCredentialStoreExportAttachment(final SOSOptionBoolean val) {
        credentialStoreExportAttachment = val;
    }

    @JSOptionDefinition(name = "CredentialStore_ExportAttachment2FileName", description = "", key = "CredentialStore_ExportAttachment2FileName", type = "SOSOptionOutFileName", mandatory = false)
    public SOSOptionOutFileName credentialStoreExportAttachment2FileName = new SOSOptionOutFileName(this, className
            + ".CredentialStore_ExportAttachment2FileName", "", " ", " ", false);
    public SOSOptionOutFileName csExportAttachment2FileName = (SOSOptionOutFileName) credentialStoreExportAttachment2FileName.setAlias(className
            + ".CS_ExportAttachment2FileName");

    public SOSOptionOutFileName getCredentialStoreExportAttachment2FileName() {
        return credentialStoreExportAttachment2FileName;
    }

    public void setCredentialStoreExportAttachment2FileName(final SOSOptionOutFileName val) {
        credentialStoreExportAttachment2FileName = val;
    }

    @JSOptionDefinition(name = "CredentialStore_FileName", description = "", key = "CredentialStore_FileName", type = "SOSOptionInFileName", mandatory = true)
    public SOSOptionInFileName credentialStoreFileName = new SOSOptionInFileName(this, className + ".CredentialStore_FileName", "", " ", " ", true);
    public SOSOptionInFileName csFileName = (SOSOptionInFileName) credentialStoreFileName.setAlias(className + ".CS_FileName");

    public SOSOptionInFileName getCredentialStoreFileName() {
        return credentialStoreFileName;
    }

    public void setCredentialStoreFileName(final SOSOptionInFileName val) {
        credentialStoreFileName = val;
    }

    @JSOptionDefinition(name = "CredentialStore_KeyFileName", description = "", key = "CredentialStore_KeyFileName", type = "SOSOptionInFileName", mandatory = false)
    public SOSOptionInFileName credentialStoreKeyFileName = new SOSOptionInFileName(this, className + ".CredentialStore_KeyFileName", "", " ", " ",
            false);
    public SOSOptionInFileName csKeyFileName = (SOSOptionInFileName) credentialStoreKeyFileName.setAlias(className + ".CS_KeyFileName");

    public SOSOptionInFileName getCredentialStore_KeyFileName() {
        return credentialStoreKeyFileName;
    }

    public void setCredentialStoreKeyFileName(final SOSOptionInFileName val) {
        credentialStoreKeyFileName = val;
    }

    @JSOptionDefinition(name = "CredentialStore_KeyPath", description = "", key = "CredentialStore_KeyPath", type = "SOSOptionString", mandatory = true)
    public SOSOptionString credentialStoreKeyPath = new SOSOptionString(this, className + ".CredentialStore_KeyPath", "", " ", " ", true);
    public SOSOptionString csKeyPath = (SOSOptionString) credentialStoreKeyPath.setAlias(className + ".CS_KeyPath");

    public SOSOptionString getCredentialStoreKeyPath() {
        return credentialStoreKeyPath;
    }

    public void setCredentialStoreKeyPath(final SOSOptionString val) {
        credentialStoreKeyPath = val;
    }

    @JSOptionDefinition(name = "CredentialStore_OverwriteExportedFile", description = "", key = "CredentialStore_OverwriteExportedFile", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean credentialStoreOverwriteExportedFile = new SOSOptionBoolean(this, className + ".CredentialStore_OverwriteExportedFile",
            "", "true", "true", false);
    public SOSOptionBoolean csOverwriteExportedFile = (SOSOptionBoolean) credentialStoreOverwriteExportedFile.setAlias(className
            + ".CS_OverwriteExportedFile");

    public SOSOptionBoolean getCredentialStoreOverwriteExportedFile() {
        return credentialStoreOverwriteExportedFile;
    }

    public void setCredentialStoreOverwriteExportedFile(final SOSOptionBoolean val) {
        credentialStoreOverwriteExportedFile = val;
    }

    @JSOptionDefinition(name = "CredentialStore_Permissions4ExportedFile", description = "", key = "CredentialStore_Permissions4ExportedFile", type = "SOSOptionString", mandatory = false)
    public SOSOptionString credentialStorePermissions4ExportedFile = new SOSOptionString(this, className
            + ".CredentialStore_Permissions4ExportedFile", "", "600", "600", false);
    public SOSOptionString csPermissions4ExportedFile = (SOSOptionString) credentialStorePermissions4ExportedFile.setAlias(className
            + ".CS_Permissions4ExportedFile");

    public SOSOptionString getCredentialStorePermissions4ExportedFile() {
        return credentialStorePermissions4ExportedFile;
    }

    public void setCredentialStorePermissions4ExportedFile(final SOSOptionString val) {
        credentialStorePermissions4ExportedFile = val;
    }

    @JSOptionDefinition(name = "CredentialStore_ProcessNotesParams", description = "", key = "CredentialStore_ProcessNotesParams", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean credentialStoreProcessNotesParams = new SOSOptionBoolean(this, className + ".CredentialStore_ProcessNotesParams", "",
            "false", "false", false);
    public SOSOptionBoolean csProcessNotesParams = (SOSOptionBoolean) credentialStoreProcessNotesParams.setAlias(className
            + ".CS_ProcessNotesParams");

    public SOSOptionBoolean getCredentialStoreProcessNotesParams() {
        return credentialStoreProcessNotesParams;
    }

    public void setCredentialStoreProcessNotesParams(final SOSOptionBoolean val) {
        credentialStoreProcessNotesParams = val;
    }

    @JSOptionDefinition(name = "CredentialStore_StoreType", description = "", key = "CredentialStore_StoreType", type = "SOSOptionString", mandatory = false)
    public SOSOptionString credentialStoreStoreType = new SOSOptionString(this, className + ".CredentialStore_StoreType", "", "KeePass", "KeePass",
            false);
    public SOSOptionString csStoreType = (SOSOptionString) credentialStoreStoreType.setAlias(className + ".CS_StoreType");

    public SOSOptionString getCredentialStoreStoreType() {
        return credentialStoreStoreType;
    }

    public void setCredentialStoreStoreType(final SOSOptionString val) {
        credentialStoreStoreType = val;
    }

    @JSOptionDefinition(name = "CredentialStore_password", description = "", key = "CredentialStore_password", type = "SOSOptionPassword", mandatory = false)
    public SOSOptionPassword credentialStorePassword = new SOSOptionPassword(this, className + ".CredentialStore_password", "", " ", " ", false);
    public SOSOptionPassword CS_password = (SOSOptionPassword) credentialStorePassword.setAlias(className + ".CS_password");

    public SOSOptionPassword getCredentialStorePassword() {
        return credentialStorePassword;
    }

    public void setCredentialStorePassword(final SOSOptionPassword val) {
        credentialStorePassword = val;
    }

    @JSOptionDefinition(name = "use_credential_Store", description = "", key = "use_credential_Store", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean useCredentialStore = new SOSOptionBoolean(this, className + ".use_credential_Store", "", "false", "false", false);

    public SOSOptionBoolean getUseCredentialStore() {
        return useCredentialStore;
    }

    public void setUseCredentialStore(final SOSOptionBoolean val) {
        useCredentialStore = val;
    }

    public SOSCredentialStoreOptionsSuperClass() {
        objParentClass = this.getClass();
    }

    public SOSCredentialStoreOptionsSuperClass(final JSListener listener) {
        this();
        this.registerMessageListener(listener);
    }

    public SOSCredentialStoreOptionsSuperClass(final HashMap<String, String> settings) throws Exception {
        this();
        this.setAllOptions(settings);
    }

    @Override
    public void setAllOptions(final HashMap<String, String> settings) {
        objSettings = settings;
        super.setSettings(objSettings);
        super.setAllOptions(settings);
    }

    @Override
    public void checkMandatory() throws JSExceptionMandatoryOptionMissing, Exception {
        try {
            super.checkMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    }
}