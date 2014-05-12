package sos.configuration;

/**
 * Diese Klasse repräsentiert eine Parameter/Einstellung mit folgenden Eigenschaften:
 * 
 *  - identifier
 *  - Name
 *  - Wert
 *  - Defaultwert
 *  - Ist password?
 *  
 * @author mo
 *
 */
public class SOSConfigurationItem {

	private String    name                 = "";
	private String    value                = "";
	private String    defaults             = "";		
	private String    itemId               = "";
	private boolean   isPassword           = false;
	
	
	/**
	 * Konstruktor
	 * 
	 */
	public SOSConfigurationItem() { 
		
	}

	/**
	 * Konstruktur
	 * @param name
	 * @param value
	 */
	public SOSConfigurationItem(String name, String value) {
		setName(name);
		setValue(value);
	}
	
	/**
	 * Konstruktor
	 * @param name
	 * @param value
	 * @param defaultvalue
	 */
	public SOSConfigurationItem(String name, String value, String defaultvalue) {
		setName(name);
		setValue(value);
		setDefaults(defaultvalue);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the value
	 */
	public String getValue() {
		if(value != null && value.length() > 0)
			return value;
		else if(defaults != null && defaults.length() > 0)
			return defaults;
		else
			return "";
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @return the defaults
	 */
	public String getDefaults() {
		return defaults;
	}


	/**
	 * @param defaults the defaults to set
	 */
	public void setDefaults(String defaults) {		
		this.defaults = defaults;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the isPassword
	 */
	public boolean isPassword() {
		return isPassword;
	}

	/**
	 * @param isPassword the isPassword to set
	 */
	public void setPassword(boolean isPassword) {
		this.isPassword = isPassword;
	}

}
