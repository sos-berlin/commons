package sos.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** @author Joacim Zschimmer */
public class SOSArguments {

    final Map arguments = new HashMap();

    static class Argument {

        final String value;
        boolean read = false;

        Argument(String value) {
            this.value = value;
        }

    }

    public SOSArguments(String argStr) throws Exception {
        String[] args = (" " + argStr).split(" [-]");
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null || args[i].isEmpty() || args[i].trim().isEmpty()) {
                continue;
            }
            String arg = "-" + args[i].trim();
            int equal = arg.indexOf("=");
            if (equal == -1) {
                throw new Exception("SOSArguments(): illegal parameter: " + arg);
            }
            if (arg.length() > equal + 1 && arg.charAt(equal + 1) == '\'' && arg.charAt(arg.length() - 1) == '\'') {
                arg = arg.substring(0, equal + 1) + arg.substring(equal + 2, arg.length() - 1);
            }
            arguments.put(arg.substring(0, equal + 1), new Argument(arg.substring(equal + 1)));
        }
    }

    public SOSArguments(String[] args) throws Exception {
        setArguments(args);
    }

    public SOSArguments(String[] args, boolean ignore) throws Exception {
        if (!ignore) {
            setArguments(args);
        } else {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                int equal = arg.indexOf("=") > -1 ? arg.indexOf("=") : 0;
                if (arg.length() > equal + 1 && arg.charAt(equal + 1) == '\'' && arg.charAt(arg.length() - 1) == '\'') {
                    arg = arg.substring(0, equal + 1) + arg.substring(equal + 2, arg.length() - 1);
                }
                arguments.put(arg.substring(0, equal + 1), new Argument(arg.substring(equal + 1)));
            }
        }
    }

    public String asString(String option) throws Exception {
        return asString(option, null);
    }

    public String asString(String option, String defaultString) throws Exception {
        Argument argument = (Argument) arguments.get(option);
        if (argument == null) {
            if (defaultString == null) {
                throw new Exception("SOSArguments.as_string(): parameter is missing: " + option);
            }
            return defaultString;
        }
        argument.read = true;
        return argument.value;
    }

    public int asInt(String option) throws Exception {
        String result = asString(option);
        try {
            return Integer.parseInt(result);
        } catch (Exception x) {
            throw new Exception("SOSArguments.asInt(): invalid parameter [" + option + "]: " + x.getMessage(), x);
        }
    }

    public int asInt(String option, int defaultInt) throws Exception {
        String result = asString(option, "");
        if ("".equals(result)) {
            return defaultInt;
        }
        return asInt(option);
    }

    public boolean asBool(String option) throws Exception {
        String result = asString(option);
        try {
            boolean ok = false;
            boolean rc = false;
            if (!ok && "true".equalsIgnoreCase(result)) {
                ok = true;
                rc = true;
            }
            if (!ok && "1".equalsIgnoreCase(result)) {
                ok = true;
                rc = true;
            }
            if (!ok && "false".equalsIgnoreCase(result)) {
                ok = true;
                rc = false;
            }
            if (!ok && "0".equalsIgnoreCase(result)) {
                ok = true;
                rc = false;
            }
            if (!ok) {
                throw new Exception("none of the values true|false was given");
            }
            return rc;
        } catch (Exception x) {
            throw new Exception("SOSArguments.asBool(): invalid parameter [" + option + "]: " + x.getMessage(), x);
        }
    }

    public boolean asBool(String option, boolean defaultBoolean) throws Exception {
        String result = asString(option, "");
        if ("".equals(result)) {
            return defaultBoolean;
        }
        return asBool(option);
    }

    public void checkAllUsed() throws Exception {
        for (Iterator it = arguments.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String option = (String) entry.getKey();
            Argument argument = (Argument) entry.getValue();
            if (!argument.read) {
                throw new Exception("SOSArguments.check_all_used(): no parameter given for option: " + option);
            }
        }
    }

    private void setArguments(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (!arg.startsWith("-")) {
                throw new Exception("SOSArguments(): illegal parameter: " + arg);
            }
            int equal = arg.indexOf("=");
            if (equal == -1) {
                throw new Exception("SOSArguments(): illegal parameter: " + arg);
            }
            if (arg.length() > equal + 1 && arg.charAt(equal + 1) == '\'' && arg.charAt(arg.length() - 1) == '\'') {
                arg = arg.substring(0, equal + 1) + arg.substring(equal + 2, arg.length() - 1);
            }
            arguments.put(arg.substring(0, equal + 1), new Argument(arg.substring(equal + 1)));
        }
    }

    public static void main(String[] args) {
        try {
            SOSArguments arguments = new SOSArguments(args, true);
            Iterator keys = arguments.arguments.keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                System.out.println(key + arguments.asString(key.toString()));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public Map getArguments() {
        return arguments;
    }

}