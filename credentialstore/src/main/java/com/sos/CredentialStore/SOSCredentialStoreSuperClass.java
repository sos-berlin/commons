/**
 * 
 */
package com.sos.CredentialStore;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sos.CredentialStore.Options.ISOSCredentialStoreOptionsBridge;
import com.sos.JSHelper.Options.JSOptionsClass;
import com.sos.JSHelper.Options.SOSOptionAuthenticationMethod;
import com.sos.JSHelper.Options.SOSOptionElement;
import com.sos.JSHelper.Options.SOSOptionHostName;
import com.sos.JSHelper.Options.SOSOptionInFileName;
import com.sos.JSHelper.Options.SOSOptionPassword;
import com.sos.JSHelper.Options.SOSOptionPortNumber;
import com.sos.JSHelper.Options.SOSOptionTransferType;
import com.sos.JSHelper.Options.SOSOptionUrl;
import com.sos.JSHelper.Options.SOSOptionUserName;

/**
 * @author KB
 *
 */
public class SOSCredentialStoreSuperClass extends JSOptionsClass implements ISOSCredentialStoreOptionsBridge {
	@SuppressWarnings("unused")
	private final String				conClassName		= this.getClass().getSimpleName();
	@SuppressWarnings("unused")
	private static final String			conSVNVersion		= "$Id$";
	@SuppressWarnings("unused")
	private final Logger				logger				= Logger.getLogger(this.getClass());
	private static final long			serialVersionUID	= -6433766118396427355L;
	protected SOSCredentialStoreImpl	objCredentialStore	= null;

	/**
	 * 
	 */
	public SOSCredentialStoreSuperClass() {
		super();
	}

	/**
	 * @param pobjSettings
	 */
	public SOSCredentialStoreSuperClass(final HashMap<String, String> pobjSettings) {
		super(pobjSettings);
	}

	/**
	 *
	 * \brief CheckMandatory
	 *
	 * \details
	 * Der Aufruf dieser Methode pr�ft alle obligatorisch anzugebenden Optionen.
	 * Wird f�r eine Option kein Wert gefunden, so wird eine Exception ausgel�st.
	 *
	 * Die Methode wird grunds�tzlich von der abgeleiteten Klasse �berschrieben,
	 * da die obligatorsichen Optionen von Klasse zu Klasse unterschiedlich sind.
	 *
	 * \return void
	 *
	 * @throws Exception
	 */
	@Override
	public void CheckMandatory() throws Exception {
		getCredentialStore().checkCredentialStoreOptions();
	}


	//	Credential Store Methods and fields
	public SOSCredentialStoreImpl getCredentialStore() {
		if (objCredentialStore == null) {
			objCredentialStore = new SOSCredentialStoreImpl(this);
		}
		return objCredentialStore;
	}

	public void setChildClasses(final HashMap<String, String> pobjJSSettings, final String pstrPrefix) throws Exception {
		getCredentialStore().setChildClasses(pobjJSSettings, pstrPrefix);
		objCredentialStore.checkCredentialStoreOptions();
	} // public SOSConnection2OptionsAlternate (HashMap JSSettings)

	@Override public SOSOptionUrl getUrl() {
		return null;
	}

	@Override public void setUrl(final SOSOptionElement pstrValue) {
	}

	@Override public SOSOptionHostName getHost() {
		return null;
	}

	@Override public void setHost(final SOSOptionElement p_host) {
	}

	@Override public SOSOptionPortNumber getPort() {
		return null;
	}

	@Override public void setPort(final SOSOptionPortNumber p_port) {
	}

	@Override public SOSOptionTransferType getProtocol() {
		return null;
	}

	@Override public void setProtocol(final SOSOptionTransferType p_protocol) {
	}

	@Override public SOSOptionUserName getUser() {
		return null;
	}

	@Override public SOSOptionPassword getPassword() {
		return null;
	}

	@Override public void setPassword(final SOSOptionPassword p_password) {
	}

	@Override public SOSOptionInFileName getAuth_file() {
		return null;
	}

	@Override public void setAuth_file(final SOSOptionInFileName p_ssh_auth_file) {
	}

	@Override public SOSOptionAuthenticationMethod getAuth_method() {
		return null;
	}

	@Override public void setAuth_method(final SOSOptionAuthenticationMethod p_ssh_auth_method) {
	}

	@Override public void setUser(final SOSOptionUserName pobjUser) {
	}
}
