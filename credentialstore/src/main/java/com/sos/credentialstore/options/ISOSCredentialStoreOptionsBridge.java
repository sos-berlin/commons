package com.sos.credentialstore.options;

import com.sos.JSHelper.Options.SOSOptionAuthenticationMethod;
import com.sos.JSHelper.Options.SOSOptionHostName;
import com.sos.JSHelper.Options.SOSOptionInFileName;
import com.sos.JSHelper.Options.SOSOptionPassword;
import com.sos.JSHelper.Options.SOSOptionPortNumber;
import com.sos.JSHelper.Options.SOSOptionTransferType;
import com.sos.JSHelper.Options.SOSOptionUrl;
import com.sos.JSHelper.Options.SOSOptionUserName;

/** @author KB */
public interface ISOSCredentialStoreOptionsBridge {

    public abstract SOSOptionUrl getUrl();

    public abstract void setUrl(SOSOptionUrl pstrValue);

    public abstract SOSOptionHostName getHost();

    public abstract void setHost(SOSOptionHostName pHost);

    public abstract SOSOptionPortNumber getPort();

    public abstract void setPort(SOSOptionPortNumber pPort);

    public abstract SOSOptionTransferType getProtocol();

    public abstract void setProtocol(SOSOptionTransferType pProtocol);

    public abstract SOSOptionUserName getUser();

    public abstract SOSOptionPassword getPassword();

    public abstract void setPassword(SOSOptionPassword pPassword);

    public abstract SOSOptionInFileName getAuthFile();

    public abstract void setAuthFile(SOSOptionInFileName pSshAuthFile);

    public abstract SOSOptionAuthenticationMethod getAuthMethod();

    public abstract void setAuthMethod(SOSOptionAuthenticationMethod pSshAuthMethod);

    public abstract void setUser(SOSOptionUserName pobjUser);

    public abstract void commandLineArgs(String[] strArgs);

    public abstract void commandLineArgs(String notesText);

}