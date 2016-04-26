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
    private final String conClassName = "SOSCredentialStoreOptionsSuperClass";

    @JSOptionDefinition(name = "CredentialStore_AuthenticationMethod", description = "", key = "CredentialStore_AuthenticationMethod",
            type = "SOSOptionString", mandatory = true)
    public SOSOptionString CredentialStore_AuthenticationMethod = new SOSOptionString(this, conClassName + ".CredentialStore_AuthenticationMethod",
            "", "privatekey", "privatekey", true);
    public SOSOptionString CS_AuthenticationMethod = (SOSOptionString) CredentialStore_AuthenticationMethod.SetAlias(conClassName
            + ".CS_AuthenticationMethod");

    public SOSOptionString getCredentialStore_AuthenticationMethod() {
        return CredentialStore_AuthenticationMethod;
    }

    public void setCredentialStore_AuthenticationMethod(final SOSOptionString p_CredentialStore_AuthenticationMethod) {
        CredentialStore_AuthenticationMethod = p_CredentialStore_AuthenticationMethod;
    }

    @JSOptionDefinition(name = "CredentialStore_DeleteExportedFileOnExit", description = "", key = "CredentialStore_DeleteExportedFileOnExit",
            type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean CredentialStore_DeleteExportedFileOnExit = new SOSOptionBoolean(this, conClassName
            + ".CredentialStore_DeleteExportedFileOnExit", "", "true", "true", false);
    public SOSOptionBoolean CS_DeleteExportedFileOnExit = (SOSOptionBoolean) CredentialStore_DeleteExportedFileOnExit.SetAlias(conClassName
            + ".CS_DeleteExportedFileOnExit");

    public SOSOptionBoolean getCredentialStore_DeleteExportedFileOnExit() {
        return CredentialStore_DeleteExportedFileOnExit;
    }

    public void setCredentialStore_DeleteExportedFileOnExit(final SOSOptionBoolean p_CredentialStore_DeleteExportedFileOnExit) {
        CredentialStore_DeleteExportedFileOnExit = p_CredentialStore_DeleteExportedFileOnExit;
    }

    @JSOptionDefinition(name = "CredentialStore_ExportAttachment", description = "", key = "CredentialStore_ExportAttachment",
            type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean CredentialStore_ExportAttachment = new SOSOptionBoolean(this, conClassName + ".CredentialStore_ExportAttachment", "",
            "false", "false", false);
    public SOSOptionBoolean CS_ExportAttachment = (SOSOptionBoolean) CredentialStore_ExportAttachment.SetAlias(conClassName + ".CS_ExportAttachment");

    public SOSOptionBoolean getCredentialStore_ExportAttachment() {
        return CredentialStore_ExportAttachment;
    }

    public void setCredentialStore_ExportAttachment(final SOSOptionBoolean p_CredentialStore_ExportAttachment) {
        CredentialStore_ExportAttachment = p_CredentialStore_ExportAttachment;
    }

    @JSOptionDefinition(name = "CredentialStore_ExportAttachment2FileName", description = "", key = "CredentialStore_ExportAttachment2FileName",
            type = "SOSOptionOutFileName", mandatory = false)
    public SOSOptionOutFileName CredentialStore_ExportAttachment2FileName = new SOSOptionOutFileName(this, conClassName
            + ".CredentialStore_ExportAttachment2FileName", "", " ", " ", false);
    public SOSOptionOutFileName CS_ExportAttachment2FileName = (SOSOptionOutFileName) CredentialStore_ExportAttachment2FileName.SetAlias(conClassName
            + ".CS_ExportAttachment2FileName");

    public SOSOptionOutFileName getCredentialStore_ExportAttachment2FileName() {
        return CredentialStore_ExportAttachment2FileName;
    }

    public void setCredentialStore_ExportAttachment2FileName(final SOSOptionOutFileName p_CredentialStore_ExportAttachment2FileName) {
        CredentialStore_ExportAttachment2FileName = p_CredentialStore_ExportAttachment2FileName;
    }

    @JSOptionDefinition(name = "CredentialStore_FileName", description = "", key = "CredentialStore_FileName", type = "SOSOptionInFileName",
            mandatory = true)
    public SOSOptionInFileName CredentialStore_FileName = new SOSOptionInFileName(this, conClassName + ".CredentialStore_FileName", "", " ", " ",
            true);
    public SOSOptionInFileName CS_FileName = (SOSOptionInFileName) CredentialStore_FileName.SetAlias(conClassName + ".CS_FileName");

    public SOSOptionInFileName getCredentialStore_FileName() {
        return CredentialStore_FileName;
    }

    public void setCredentialStore_FileName(final SOSOptionInFileName p_CredentialStore_FileName) {
        CredentialStore_FileName = p_CredentialStore_FileName;
    }

    @JSOptionDefinition(name = "CredentialStore_KeyFileName", description = "", key = "CredentialStore_KeyFileName", type = "SOSOptionInFileName",
            mandatory = false)
    public SOSOptionInFileName CredentialStore_KeyFileName = new SOSOptionInFileName(this, conClassName + ".CredentialStore_KeyFileName", "", " ",
            " ", false);
    public SOSOptionInFileName CS_KeyFileName = (SOSOptionInFileName) CredentialStore_KeyFileName.SetAlias(conClassName + ".CS_KeyFileName");

    public SOSOptionInFileName getCredentialStore_KeyFileName() {
        return CredentialStore_KeyFileName;
    }

    public void setCredentialStore_KeyFileName(final SOSOptionInFileName p_CredentialStore_KeyFileName) {
        CredentialStore_KeyFileName = p_CredentialStore_KeyFileName;
    }

    @JSOptionDefinition(name = "CredentialStore_KeyPath", description = "", key = "CredentialStore_KeyPath", type = "SOSOptionString",
            mandatory = true)
    public SOSOptionString CredentialStore_KeyPath = new SOSOptionString(this, conClassName + ".CredentialStore_KeyPath", "", " ", " ", true);
    public SOSOptionString CS_KeyPath = (SOSOptionString) CredentialStore_KeyPath.SetAlias(conClassName + ".CS_KeyPath");

    public SOSOptionString getCredentialStore_KeyPath() {
        return CredentialStore_KeyPath;
    }

    public void setCredentialStore_KeyPath(final SOSOptionString p_CredentialStore_KeyPath) {
        CredentialStore_KeyPath = p_CredentialStore_KeyPath;
    }

    @JSOptionDefinition(name = "CredentialStore_OverwriteExportedFile", description = "", key = "CredentialStore_OverwriteExportedFile",
            type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean CredentialStore_OverwriteExportedFile = new SOSOptionBoolean(this, 
            conClassName + ".CredentialStore_OverwriteExportedFile", "", "true", "true", false);
    public SOSOptionBoolean CS_OverwriteExportedFile = (SOSOptionBoolean) CredentialStore_OverwriteExportedFile.SetAlias(conClassName
            + ".CS_OverwriteExportedFile");

    public SOSOptionBoolean getCredentialStore_OverwriteExportedFile() {
        return CredentialStore_OverwriteExportedFile;
    }

    public void setCredentialStore_OverwriteExportedFile(final SOSOptionBoolean p_CredentialStore_OverwriteExportedFile) {
        CredentialStore_OverwriteExportedFile = p_CredentialStore_OverwriteExportedFile;
    }

    @JSOptionDefinition(name = "CredentialStore_Permissions4ExportedFile", description = "", key = "CredentialStore_Permissions4ExportedFile",
            type = "SOSOptionString", mandatory = false)
    public SOSOptionString CredentialStore_Permissions4ExportedFile = new SOSOptionString(this, conClassName
            + ".CredentialStore_Permissions4ExportedFile", "", "600", "600", false);
    public SOSOptionString CS_Permissions4ExportedFile = (SOSOptionString) CredentialStore_Permissions4ExportedFile.SetAlias(conClassName
            + ".CS_Permissions4ExportedFile");

    public SOSOptionString getCredentialStore_Permissions4ExportedFile() {
        return CredentialStore_Permissions4ExportedFile;
    }

    public void setCredentialStore_Permissions4ExportedFile(final SOSOptionString p_CredentialStore_Permissions4ExportedFile) {
        CredentialStore_Permissions4ExportedFile = p_CredentialStore_Permissions4ExportedFile;
    }

    @JSOptionDefinition(name = "CredentialStore_ProcessNotesParams", description = "", key = "CredentialStore_ProcessNotesParams",
            type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean CredentialStore_ProcessNotesParams = new SOSOptionBoolean(this, conClassName + ".CredentialStore_ProcessNotesParams",
            "", "false", "false", false);
    public SOSOptionBoolean CS_ProcessNotesParams = (SOSOptionBoolean) CredentialStore_ProcessNotesParams.SetAlias(conClassName
            + ".CS_ProcessNotesParams");

    public SOSOptionBoolean getCredentialStore_ProcessNotesParams() {
        return CredentialStore_ProcessNotesParams;
    }

    public void setCredentialStore_ProcessNotesParams(final SOSOptionBoolean p_CredentialStore_ProcessNotesParams) {
        CredentialStore_ProcessNotesParams = p_CredentialStore_ProcessNotesParams;
    }

    @JSOptionDefinition(name = "CredentialStore_StoreType", description = "", key = "CredentialStore_StoreType", type = "SOSOptionString",
            mandatory = false)
    public SOSOptionString CredentialStore_StoreType = new SOSOptionString(this, conClassName + ".CredentialStore_StoreType", "", "KeePass",
            "KeePass", false);
    public SOSOptionString CS_StoreType = (SOSOptionString) CredentialStore_StoreType.SetAlias(conClassName + ".CS_StoreType");

    public SOSOptionString getCredentialStore_StoreType() {
        return CredentialStore_StoreType;
    }

    public void setCredentialStore_StoreType(final SOSOptionString p_CredentialStore_StoreType) {
        CredentialStore_StoreType = p_CredentialStore_StoreType;
    }

    @JSOptionDefinition(name = "CredentialStore_password", description = "", key = "CredentialStore_password", type = "SOSOptionPassword",
            mandatory = false)
    public SOSOptionPassword CredentialStore_password = new SOSOptionPassword(this, conClassName + ".CredentialStore_password", "", " ", " ", false);
    public SOSOptionPassword CS_password = (SOSOptionPassword) CredentialStore_password.SetAlias(conClassName + ".CS_password");

    public SOSOptionPassword getCredentialStore_password() {
        return CredentialStore_password;
    }

    public void setCredentialStore_password(final SOSOptionPassword p_CredentialStore_password) {
        CredentialStore_password = p_CredentialStore_password;
    }

    @JSOptionDefinition(name = "use_credential_Store", description = "", key = "use_credential_Store", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean use_credential_Store = new SOSOptionBoolean(this, conClassName + ".use_credential_Store", "", "false", "false", false);

    public SOSOptionBoolean getuse_credential_Store() {
        return use_credential_Store;
    }

    public void setuse_credential_Store(final SOSOptionBoolean p_use_credential_Store) {
        use_credential_Store = p_use_credential_Store;
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
        flgSetAllOptions = true;
        objSettings = pobjJSSettings;
        super.Settings(objSettings);
        super.setAllOptions(pobjJSSettings);
        flgSetAllOptions = false;
    }

    @Override
    public void CheckMandatory() throws JSExceptionMandatoryOptionMissing, Exception {
        try {
            super.CheckMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    }

    @Override
    public void CommandLineArgs(final String[] pstrArgs) {
        super.CommandLineArgs(pstrArgs);
    }
    
}