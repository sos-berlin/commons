package com.sos.CredentialStore;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sos.CredentialStore.KeePass.pl.sind.keepass.kdb.KeePassDataBase;
import com.sos.CredentialStore.KeePass.pl.sind.keepass.kdb.KeePassDataBaseManager;
import com.sos.CredentialStore.KeePass.pl.sind.keepass.kdb.v1.Entry;
import com.sos.CredentialStore.KeePass.pl.sind.keepass.kdb.v1.KeePassDataBaseV1;
import com.sos.CredentialStore.Options.ISOSCredentialStoreOptionsBridge;
import com.sos.CredentialStore.Options.SOSCredentialStoreOptions;
import com.sos.CredentialStore.exceptions.CredentialStoreEntryExpired;
import com.sos.CredentialStore.exceptions.CredentialStoreKeyNotFound;
import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.JSHelper.Options.SOSOptionElement;

public class SOSCredentialStoreImpl extends JSToolBox {

    private static final Logger LOGGER = Logger.getLogger(SOSCredentialStoreImpl.class);
    private ISOSCredentialStoreOptionsBridge objOptionsBridge = null;
    @JSOptionClass(description = "", name = "SOSCredentialStoreOptions")
    private SOSCredentialStoreOptions objCredentialStoreOptions = null;
    private KeePassDataBase keePassDb = null;
    private KeePassDataBaseV1 kdb1 = null;

    public SOSCredentialStoreImpl(final ISOSCredentialStoreOptionsBridge pobjOptionsBridge) {
        objOptionsBridge = pobjOptionsBridge;
    }

    public void setChildClasses(final HashMap<String, String> pobjJSSettings, final String pstrPrefix) throws Exception {
        getCredentialStore().setAllOptions(pobjJSSettings, pstrPrefix);
    }

    public SOSCredentialStoreOptions getCredentialStore() {
        if (objCredentialStoreOptions == null) {
            objCredentialStoreOptions = new SOSCredentialStoreOptions();
        }
        checkCredentialStoreOptions();
        return objCredentialStoreOptions;
    }

    public SOSCredentialStoreOptions getOptions() {
        if (objCredentialStoreOptions == null) {
            objCredentialStoreOptions = new SOSCredentialStoreOptions();
        }
        return objCredentialStoreOptions;
    }

    public void checkCredentialStoreOptions() {
        if (getOptions().useCredentialStore.isTrue()) {
            LOGGER.trace("entering checkCredentialStoreOptions ");
            objCredentialStoreOptions.credentialStoreFileName.CheckMandatory(true);
            objCredentialStoreOptions.credentialStoreKeyPath.CheckMandatory(true);
            String strPassword = null;
            File fleKeyFile = null;
            if (objCredentialStoreOptions.credentialStoreKeyFileName.isDirty()) {
                fleKeyFile = new File(objCredentialStoreOptions.credentialStoreKeyFileName.Value());
            }
            if (objCredentialStoreOptions.credentialStorePassword.isDirty()) {
                strPassword = objCredentialStoreOptions.credentialStorePassword.Value();
            }
            File fleKeePassDataBase = new File(objCredentialStoreOptions.credentialStoreFileName.Value());
            try {
                keePassDb = KeePassDataBaseManager.openDataBase(fleKeePassDataBase, fleKeyFile, strPassword);
            } catch (Exception e) {
                LOGGER.error(e);
                throw new JobSchedulerException(e);
            }
            kdb1 = (KeePassDataBaseV1) keePassDb;
            Entry objEntry = kdb1.getEntry(objCredentialStoreOptions.credentialStoreKeyPath.Value());
            if (objEntry == null) {
                throw new CredentialStoreKeyNotFound(objCredentialStoreOptions);
            }
            Date objExpDate = objEntry.ExpirationDate();
            if (new Date().after(objExpDate)) {
                throw new CredentialStoreEntryExpired(objExpDate);
            }
            boolean flgHideValuesFromCredentialStore = false;
            if (objEntry.Url().length() > 0) {
                LOGGER.trace(objEntry.Url());
                // Possible Elements of an URL are:
                //
                // http://hans:geheim@www.example.org:80/demo/example.cgi?land=de&stadt=aa#geschichte
                // | | | | | | | |
                // | | | host | url-path searchpart fragment
                // | | password port
                // | user
                // protocol
                //
                // ftp://<user>:<password>@<host>:<port>/<url-path>;type=<typecode>
                // see
                // http://docs.oracle.com/javase/7/docs/api/java/net/URL.html
                String strUrl = objEntry.Url();
                try {
                    URL objURL = new URL(strUrl);
                    setIfNotDirty(objOptionsBridge.getHost(), objURL.getHost());
                    String strPort = String.valueOf(objURL.getPort());
                    if (isEmpty(strPort) || "-1".equals(strPort)) {
                        strPort = String.valueOf(objURL.getDefaultPort());
                    }
                    setIfNotDirty(objOptionsBridge.getPort(), strPort);
                    setIfNotDirty(objOptionsBridge.getProtocol(), objURL.getProtocol());
                    String strUserInfo = objURL.getUserInfo();
                    String[] strU = strUserInfo.split(":");
                    setIfNotDirty(objOptionsBridge.getUser(), strU[0]);
                    if (strU.length > 1) {
                        setIfNotDirty(objOptionsBridge.getPassword(), strU[1]);
                    }
                    String strAuthority = objURL.getAuthority();
                } catch (MalformedURLException e) {
                    // not a valid url. ignore it, because it could be a host
                    // name only
                }
            }
            if (isNotEmpty(objEntry.UserName())) {
                objOptionsBridge.getUser().Value(objEntry.UserName());
                objOptionsBridge.getUser().setHideValue(flgHideValuesFromCredentialStore);
            }
            if (isNotEmpty(objEntry.Password())) {
                objOptionsBridge.getPassword().Value(objEntry.Password());
                objOptionsBridge.getPassword().setHideValue(flgHideValuesFromCredentialStore);
            }
            if (isNotEmpty(objEntry.Url())) {
                objOptionsBridge.getHost().Value(objEntry.Url());
                objOptionsBridge.getHost().setHideValue(flgHideValuesFromCredentialStore);
            }
            objEntry.ExpirationDate();
            if (objOptionsBridge.getHost().isNotDirty()) {
                objOptionsBridge.getHost().Value(objEntry.getUrl().toString());
            }
            if (objCredentialStoreOptions.credentialStoreExportAttachment.isTrue()) {
                File fleO = objEntry.saveAttachmentAsFile(objCredentialStoreOptions.credentialStoreExportAttachment2FileName.Value());
                if (objCredentialStoreOptions.credentialStoreDeleteExportedFileOnExit.isTrue()) {
                    fleO.deleteOnExit();
                }
            }
            if (objCredentialStoreOptions.credentialStoreProcessNotesParams.isTrue()) {
                objOptionsBridge.commandLineArgs(objEntry.getNotesText());
            }
        }
    }

    private void setIfNotDirty(final SOSOptionElement objOption, final String pstrValue) {
        if (objOption.isNotDirty() && isNotEmpty(pstrValue)) {
            LOGGER.trace("setValue = " + pstrValue);
            objOption.Value(pstrValue);
        }
    }
}
