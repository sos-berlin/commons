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
    private static final String CLASSNAME = "SOSCredentialStoreOptionsSuperClass";

    @JSOptionDefinition(name = "CredentialStore_AuthenticationMethod", description = "", key = "CredentialStore_AuthenticationMethod",
            type = "SOSOptionString", mandatory = true)
    public SOSOptionString credentialStoreAuthenticationMethod = new SOSOptionString(this, CLASSNAME + ".CredentialStore_AuthenticationMethod",
            "", "privatekey", "privatekey", true);
    public SOSOptionString csAuthenticationMethod = (SOSOptionString) credentialStoreAuthenticationMethod.setAlias(CLASSNAME
            + ".CS_AuthenticationMethod");

    public SOSOptionString getCredentialStoreAuthenticationMethod() {
        return credentialStoreAuthenticationMethod;
    }

    public void setCredentialStoreAuthenticationMethod(final SOSOptionString pCredentialStoreAuthenticationMethod) {
        credentialStoreAuthenticationMethod = pCredentialStoreAuthenticationMethod;
    }

    @JSOptionDefinition(name = "CredentialStore_DeleteExportedFileOnExit", description = "", key = "CredentialStore_DeleteExportedFileOnExit",
            type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean credentialStoreDeleteExportedFileOnExit = new SOSOptionBoolean(this, CLASSNAME
            + ".CredentialStore_DeleteExportedFileOnExit", "", "true", "true", false);
    public SOSOptionBoolean csDeleteExportedFileOnExit = (SOSOptionBoolean) credentialStoreDeleteExportedFileOnExit.setAlias(CLASSNAME
            + ".CS_DeleteExportedFileOnExit");

    public SOSOptionBoolean getCredentialStoreDeleteExportedFileOnExit() {
        return credentialStoreDeleteExportedFileOnExit;
    }

    public void setCredentialStoreDeleteExportedFileOnExit(final SOSOptionBoolean pCredentialStoreDeleteExportedFileOnExit) {
        credentialStoreDeleteExportedFileOnExit = pCredentialStoreDeleteExportedFileOnExit;
    }

    @JSOptionDefinition(name = "CredentialStore_ExportAttachment", description = "", key = "CredentialStore_ExportAttachment",
            type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean credentialStoreExportAttachment = new SOSOptionBoolean(this, CLASSNAME + ".CredentialStore_ExportAttachment", "",
            "false", "false", false);
    public SOSOptionBoolean csExportAttachment = (SOSOptionBoolean) credentialStoreExportAttachment.setAlias(CLASSNAME + ".CS_ExportAttachment");

    public SOSOptionBoolean getCredentialStoreExportAttachment() {
        return credentialStoreExportAttachment;
    }

    public void setCredentialStoreExportAttachment(final SOSOptionBoolean pCredentialStoreExportAttachment) {
        credentialStoreExportAttachment = pCredentialStoreExportAttachment;
    }

    @JSOptionDefinition(name = "CredentialStore_ExportAttachment2FileName", description = "", key = "CredentialStore_ExportAttachment2FileName",
            type = "SOSOptionOutFileName", mandatory = false)
    public SOSOptionOutFileName credentialStoreExportAttachment2FileName = new SOSOptionOutFileName(this, CLASSNAME
            + ".CredentialStore_ExportAttachment2FileName", "", " ", " ", false);
    public SOSOptionOutFileName csExportAttachment2FileName = (SOSOptionOutFileName) credentialStoreExportAttachment2FileName.setAlias(CLASSNAME
            + ".CS_ExportAttachment2FileName");

    public SOSOptionOutFileName getCredentialStoreExportAttachment2FileName() {
        return credentialStoreExportAttachment2FileName;
    }

    public void setCredentialStoreExportAttachment2FileName(final SOSOptionOutFileName pCredentialStoreExportAttachment2FileName) {
        credentialStoreExportAttachment2FileName = pCredentialStoreExportAttachment2FileName;
    }

    @JSOptionDefinition(name = "CredentialStore_FileName", description = "", key = "CredentialStore_FileName", type = "SOSOptionInFileName",
            mandatory = true)
    public SOSOptionInFileName credentialStoreFileName = new SOSOptionInFileName(this, CLASSNAME + ".CredentialStore_FileName", "", " ", " ",
            true);
    public SOSOptionInFileName csFileName = (SOSOptionInFileName) credentialStoreFileName.setAlias(CLASSNAME + ".CS_FileName");

    public SOSOptionInFileName getCredentialStoreFileName() {
        return credentialStoreFileName;
    }

    public void setCredentialStoreFileName(final SOSOptionInFileName pCredentialStoreFileName) {
        credentialStoreFileName = pCredentialStoreFileName;
    }

    @JSOptionDefinition(name = "CredentialStore_KeyFileName", description = "", key = "CredentialStore_KeyFileName", type = "SOSOptionInFileName",
            mandatory = false)
    public SOSOptionInFileName credentialStoreKeyFileName = new SOSOptionInFileName(this, CLASSNAME + ".CredentialStore_KeyFileName", "", " ",
            " ", false);
    public SOSOptionInFileName csKeyFileName = (SOSOptionInFileName) credentialStoreKeyFileName.setAlias(CLASSNAME + ".CS_KeyFileName");

    public SOSOptionInFileName getCredentialStore_KeyFileName() {
        return credentialStoreKeyFileName;
    }

    public void setCredentialStoreKeyFileName(final SOSOptionInFileName pCredentialStoreKeyFileName) {
        credentialStoreKeyFileName = pCredentialStoreKeyFileName;
    }

    @JSOptionDefinition(name = "CredentialStore_KeyPath", description = "", key = "CredentialStore_KeyPath", type = "SOSOptionString",
            mandatory = true)
    public SOSOptionString credentialStoreKeyPath = new SOSOptionString(this, CLASSNAME + ".CredentialStore_KeyPath", "", " ", " ", true);
    public SOSOptionString csKeyPath = (SOSOptionString) credentialStoreKeyPath.setAlias(CLASSNAME + ".CS_KeyPath");

    public SOSOptionString getCredentialStoreKeyPath() {
        return credentialStoreKeyPath;
    }

    public void setCredentialStoreKeyPath(final SOSOptionString pCredentialStoreKeyPath) {
        credentialStoreKeyPath = pCredentialStoreKeyPath;
    }

    @JSOptionDefinition(name = "CredentialStore_OverwriteExportedFile", description = "", key = "CredentialStore_OverwriteExportedFile",
            type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean credentialStoreOverwriteExportedFile = new SOSOptionBoolean(this, 
            CLASSNAME + ".CredentialStore_OverwriteExportedFile", "", "true", "true", false);
    public SOSOptionBoolean csOverwriteExportedFile = (SOSOptionBoolean) credentialStoreOverwriteExportedFile.setAlias(CLASSNAME
            + ".CS_OverwriteExportedFile");

    public SOSOptionBoolean getCredentialStoreOverwriteExportedFile() {
        return credentialStoreOverwriteExportedFile;
    }

    public void setCredentialStoreOverwriteExportedFile(final SOSOptionBoolean pCredentialStoreOverwriteExportedFile) {
        credentialStoreOverwriteExportedFile = pCredentialStoreOverwriteExportedFile;
    }

    @JSOptionDefinition(name = "CredentialStore_Permissions4ExportedFile", description = "", key = "CredentialStore_Permissions4ExportedFile",
            type = "SOSOptionString", mandatory = false)
    public SOSOptionString credentialStorePermissions4ExportedFile = new SOSOptionString(this, CLASSNAME
            + ".CredentialStore_Permissions4ExportedFile", "", "600", "600", false);
    public SOSOptionString csPermissions4ExportedFile = (SOSOptionString) credentialStorePermissions4ExportedFile.setAlias(CLASSNAME
            + ".CS_Permissions4ExportedFile");

    public SOSOptionString getCredentialStorePermissions4ExportedFile() {
        return credentialStorePermissions4ExportedFile;
    }

    public void setCredentialStorePermissions4ExportedFile(final SOSOptionString pCredentialStorePermissions4ExportedFile) {
        credentialStorePermissions4ExportedFile = pCredentialStorePermissions4ExportedFile;
    }

    @JSOptionDefinition(name = "CredentialStore_ProcessNotesParams", description = "", key = "CredentialStore_ProcessNotesParams",
            type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean credentialStoreProcessNotesParams = new SOSOptionBoolean(this, CLASSNAME + ".CredentialStore_ProcessNotesParams",
            "", "false", "false", false);
    public SOSOptionBoolean csProcessNotesParams = (SOSOptionBoolean) credentialStoreProcessNotesParams.setAlias(CLASSNAME
            + ".CS_ProcessNotesParams");

    public SOSOptionBoolean getCredentialStoreProcessNotesParams() {
        return credentialStoreProcessNotesParams;
    }

    public void setCredentialStoreProcessNotesParams(final SOSOptionBoolean pCredentialStoreProcessNotesParams) {
        credentialStoreProcessNotesParams = pCredentialStoreProcessNotesParams;
    }

    @JSOptionDefinition(name = "CredentialStore_StoreType", description = "", key = "CredentialStore_StoreType", type = "SOSOptionString",
            mandatory = false)
    public SOSOptionString credentialStoreStoreType = new SOSOptionString(this, CLASSNAME + ".CredentialStore_StoreType", "", "KeePass",
            "KeePass", false);
    public SOSOptionString csStoreType = (SOSOptionString) credentialStoreStoreType.setAlias(CLASSNAME + ".CS_StoreType");

    public SOSOptionString getCredentialStoreStoreType() {
        return credentialStoreStoreType;
    }

    public void setCredentialStoreStoreType(final SOSOptionString pCredentialStoreStoreType) {
        credentialStoreStoreType = pCredentialStoreStoreType;
    }

    @JSOptionDefinition(name = "CredentialStore_password", description = "", key = "CredentialStore_password", type = "SOSOptionPassword",
            mandatory = false)
    public SOSOptionPassword credentialStorePassword = new SOSOptionPassword(this, CLASSNAME + ".CredentialStore_password", "", " ", " ", false);
    public SOSOptionPassword CS_password = (SOSOptionPassword) credentialStorePassword.setAlias(CLASSNAME + ".CS_password");

    public SOSOptionPassword getCredentialStorePassword() {
        return credentialStorePassword;
    }

    public void setCredentialStorePassword(final SOSOptionPassword pCredentialStorePassword) {
        credentialStorePassword = pCredentialStorePassword;
    }

    @JSOptionDefinition(name = "use_credential_Store", description = "", key = "use_credential_Store", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean useCredentialStore = new SOSOptionBoolean(this, CLASSNAME + ".use_credential_Store", "", "false", "false", false);

    public SOSOptionBoolean getUseCredentialStore() {
        return useCredentialStore;
    }

    public void setUseCredentialStore(final SOSOptionBoolean pUseCredentialStore) {
        useCredentialStore = pUseCredentialStore;
    }

    public SOSCredentialStoreOptionsSuperClass() {
        objParentClass = this.getClass();
    }

    public SOSCredentialStoreOptionsSuperClass(final JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    }

    public SOSCredentialStoreOptionsSuperClass(final HashMap<String, String> JSSettings) throws Exception {
        this();
        this.setAllOptions(JSSettings);
    }

    @Override
    public void setAllOptions(final HashMap<String, String> pobjJSSettings) {
        objSettings = pobjJSSettings;
        super.setSettings(objSettings);
        super.setAllOptions(pobjJSSettings);
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