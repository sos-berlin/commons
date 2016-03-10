package sos.configuration;

/** @author mo */
public class SOSConfigurationItem {

    private String name = "";
    private String value = "";
    private String defaults = "";
    private String itemId = "";
    private boolean isPassword = false;

    public SOSConfigurationItem() {

    }

    public SOSConfigurationItem(String name, String value) {
        setName(name);
        setValue(value);
    }

    public SOSConfigurationItem(String name, String value, String defaultvalue) {
        setName(name);
        setValue(value);
        setDefaults(defaultvalue);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        if (value != null && !value.isEmpty()) {
            return value;
        } else if (defaults != null && !defaults.isEmpty()) {
            return defaults;
        } else {
            return "";
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean isPassword() {
        return isPassword;
    }

    public void setPassword(boolean isPassword) {
        this.isPassword = isPassword;
    }

}
