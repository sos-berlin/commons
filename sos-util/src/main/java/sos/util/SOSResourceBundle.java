package sos.util;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** @author Robert Ehrlich */
public class SOSResourceBundle {

    private final static Hashtable bundles = new Hashtable();
    private ResourceBundle bundle = null;
    private Locale locale = Locale.UK;

    public SOSResourceBundle() {
        this(null);
    }

    public SOSResourceBundle(Locale locale) {
        if (locale != null) {
            this.locale = locale;
        }

    }

    public void setBundle(String name) throws Exception {
        this.setBundle(name, null);
    }

    public void setBundle(String name, Locale locale) throws Exception {
        if (locale != null) {
            this.locale = locale;
        }
        if (this.locale == null) {
            this.locale = Locale.getDefault();
        }
        String bundleKey = name + "_" + this.locale.toString();
        if (bundles.containsKey(bundleKey)) {
            this.bundle = (ResourceBundle) bundles.get(bundleKey);
        } else {
            try {
                this.bundle = ResourceBundle.getBundle(name, this.locale);
                bundles.put(bundleKey, bundle);
            } catch (MissingResourceException exc) {
                throw new Exception("Error while setting ResourceBundle. Can not find : " + name);
            }
        }
    }

    public ResourceBundle getBundle() {
        return this.bundle;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getMessage(String name) throws Exception {
        try {
            return bundle.getString(name);
        } catch (MissingResourceException exc) {
            return name;
        }

    }

    public String getMessage(String name, String arg) throws Exception {
        return this.getMessage(name, new String[] { arg });
    }

    public String getMessage(String name, String arg0, String arg1) throws Exception {
        return this.getMessage(name, new String[] { arg0, arg1 });
    }

    public String getMessage(String name, String[] args) throws Exception {
        try {
            String pattern = bundle.getString(name);
            return MessageFormat.format(pattern, args);
        } catch (MissingResourceException exc) {
            return name;
        }
    }

}