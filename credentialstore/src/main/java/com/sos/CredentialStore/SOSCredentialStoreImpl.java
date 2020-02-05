package com.sos.CredentialStore;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.linguafranca.pwdb.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.CredentialStore.Options.ISOSCredentialStoreOptionsBridge;
import com.sos.CredentialStore.Options.SOSCredentialStoreOptions;
import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.JSHelper.Options.SOSOptionElement;
import com.sos.keepass.SOSKeePassDatabase;

public class SOSCredentialStoreImpl extends JSToolBox {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSCredentialStoreImpl.class);
    @JSOptionClass(description = "", name = "SOSCredentialStoreOptions")
    private SOSCredentialStoreOptions options = null;
    private ISOSCredentialStoreOptionsBridge optionsBridge = null;

    public SOSCredentialStoreImpl(final ISOSCredentialStoreOptionsBridge ob) {
        optionsBridge = ob;
    }

    public void setChildClasses(final HashMap<String, String> settings, final String prefix) throws Exception {
        getCredentialStore().setAllOptions(settings, prefix);
    }

    public SOSCredentialStoreOptions getCredentialStore() {
        if (options == null) {
            options = new SOSCredentialStoreOptions();
        }
        checkCredentialStoreOptions();
        return options;
    }

    public SOSCredentialStoreOptions getOptions() {
        if (options == null) {
            options = new SOSCredentialStoreOptions();
        }
        return options;
    }

    public void checkCredentialStoreOptions() {
        if (getOptions().useCredentialStore.isTrue()) {
            LOGGER.trace("entering checkCredentialStoreOptions ");
            options.credentialStoreFileName.checkMandatory(true);
            options.credentialStoreKeyPath.checkMandatory(true);
            String keePassPassword = null;
            String keePassKeyFile = null;
            if (options.credentialStorePassword.isDirty()) {
                keePassPassword = options.credentialStorePassword.getValue();
            }
            if (options.credentialStoreKeyFileName.isDirty()) {
                keePassKeyFile = options.credentialStoreKeyFileName.getValue();
            }

            Path keePassFile = Paths.get(options.credentialStoreFileName.getValue());
            SOSKeePassDatabase kpd = null;
            Entry<?, ?, ?, ?> entry = null;
            try {
                kpd = new SOSKeePassDatabase(keePassFile);
                kpd.load(keePassPassword, Paths.get(keePassKeyFile));
                entry = kpd.getEntryByPath(options.credentialStoreKeyPath.getValue());
                if (entry == null) {
                    throw new Exception(String.format("[%s][%s]entry not found", options.credentialStoreFileName.getValue(),
                            options.credentialStoreKeyPath.getValue()));
                }
                if (entry.getExpires()) {
                    throw new Exception(String.format("[%s][%s]entry is expired (%s)", options.credentialStoreFileName.getValue(),
                            options.credentialStoreKeyPath.getValue(), entry.getExpiryTime()));
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                throw new JobSchedulerException(e);
            }
            if (entry.getUrl().length() > 0) {
                LOGGER.trace(entry.getUrl());
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
                try {
                    URL url = new URL(entry.getUrl());
                    setIfNotDirty(optionsBridge.getHost(), url.getHost());
                    String urlPort = String.valueOf(url.getPort());
                    if (isEmpty(urlPort) || "-1".equals(urlPort)) {
                        urlPort = String.valueOf(url.getDefaultPort());
                    }
                    setIfNotDirty(optionsBridge.getPort(), urlPort);
                    setIfNotDirty(optionsBridge.getProtocol(), url.getProtocol());
                    String urlUserInfo = url.getUserInfo();
                    String[] ui = urlUserInfo.split(":");
                    setIfNotDirty(optionsBridge.getUser(), ui[0]);
                    if (ui.length > 1) {
                        setIfNotDirty(optionsBridge.getPassword(), ui[1]);
                    }
                } catch (MalformedURLException e) {
                    // not a valid url. ignore it, because it could be a host
                    // name only
                }
            }
            boolean hideValue = false;
            if (isNotEmpty(entry.getUsername())) {
                optionsBridge.getUser().setValue(entry.getUsername());
                optionsBridge.getUser().setHideValue(hideValue);
            }
            if (isNotEmpty(entry.getPassword())) {
                optionsBridge.getPassword().setValue(entry.getPassword());
                optionsBridge.getPassword().setHideValue(hideValue);
            }
            if (isNotEmpty(entry.getUrl())) {
                optionsBridge.getHost().setValue(entry.getUrl());
                optionsBridge.getHost().setHideValue(hideValue);
            }
            if (optionsBridge.getHost().isNotDirty()) {
                optionsBridge.getHost().setValue(entry.getUrl().toString());
            }
            if (options.credentialStoreExportAttachment.isTrue()) {
                Path attachmentTargetFile = Paths.get(options.credentialStoreExportAttachment2FileName.getValue());
                try {
                    kpd.exportAttachment2File(entry, attachmentTargetFile);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    throw new JobSchedulerException(e);
                }
                if (options.credentialStoreDeleteExportedFileOnExit.isTrue()) {
                    attachmentTargetFile.toFile().deleteOnExit();
                }
            }
            if (options.credentialStoreProcessNotesParams.isTrue()) {
                optionsBridge.commandLineArgs(entry.getNotes());
            }
        }
    }

    private void setIfNotDirty(final SOSOptionElement element, final String value) {
        if (element.isNotDirty() && isNotEmpty(value)) {
            LOGGER.trace("setValue = " + value);
            element.setValue(value);
        }
    }
}
