package sos.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SOSString {

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String toString(Object o) {
        return toString(o, null);
    }

    public static String toString(Object o, Collection<String> excludeFieldNames) {
        if (o == null) {
            return null;
        }
        try {
            ReflectionToStringBuilder builder = new ReflectionToStringBuilder(o, ToStringStyle.SHORT_PREFIX_STYLE) {

                @Override
                protected Object getValue(Field field) throws IllegalArgumentException, IllegalAccessException {
                    Object val = field.get(this.getObject());
                    if (val != null && val instanceof String) {
                        String v = val.toString();
                        if (v.length() > 255) {
                            val = v.substring(0, 255) + "<truncated>";
                        }
                    }
                    return val;
                }
            };
            if (excludeFieldNames != null) {
                builder.setExcludeFieldNames(excludeFieldNames.stream().toArray(String[]::new));
            }
            return builder.toString();
        } catch (Throwable t) {
        }
        return o.toString();
    }

    public boolean parseToBoolean(Object key) throws Exception {
        try {
            if (key != null) {
                return "true".equalsIgnoreCase(key.toString()) || "yes".equalsIgnoreCase(key.toString()) || "1".equals(key);
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception("..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public String parseToString(Map<String, String> hash, String key) throws Exception {
        try {
            if (hash != null && hash.get(key) != null) {
                return hash.get(key);
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new Exception("..error in " + SOSClassUtil.getMethodName() + " " + e);
        }

    }

    public String parseToString(Object key) throws Exception {
        try {
            if (key != null) {
                return key.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new Exception("..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public String parseToString(java.util.Properties prop, String key) throws Exception {
        try {
            if (prop.get(key) != null) {
                return prop.get(key).toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new Exception("..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

}