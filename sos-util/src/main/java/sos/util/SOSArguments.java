package sos.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** @author Joacim Zschimmer */
public class SOSArguments {

    final Map _arguments = new HashMap();

    static class Argument {

        final String _value;
        boolean _read = false;

        Argument(String value) {
            _value = value;
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
            if ((arg.length() > equal + 1) && arg.charAt(equal + 1) == '\'' && arg.charAt(arg.length() - 1) == '\'') {
                arg = arg.substring(0, equal + 1) + arg.substring(equal + 2, arg.length() - 1);
            }
            _arguments.put(arg.substring(0, equal + 1), new Argument(arg.substring(equal + 1)));
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
                if ((arg.length() > equal + 1) && arg.charAt(equal + 1) == '\'' && arg.charAt(arg.length() - 1) == '\'') {
                    arg = arg.substring(0, equal + 1) + arg.substring(equal + 2, arg.length() - 1);
                }
                _arguments.put(arg.substring(0, equal + 1), new Argument(arg.substring(equal + 1)));
            }
        }
    }

    public String as_string(String option) throws Exception {
        return as_string(option, null);
    }

    public String as_string(String option, String default_) throws Exception {
        Argument argument = (Argument) _arguments.get(option);
        if (argument == null) {
            if (default_ == null) {
                throw new Exception("SOSArguments.as_string(): parameter is missing: " + option);
            }
            return default_;
        }
        argument._read = true;
        return argument._value;
    }

    public int as_int(String option) throws Exception {
        String result = as_string(option);
        try {
            return Integer.parseInt(result);
        } catch (Exception x) {
            throw new Exception("SOSArguments.as_int(): invalid parameter [" + option + "]: " + x.getMessage(), x);
        }
    }

    public int as_int(String option, int default_) throws Exception {
        String result = as_string(option, "");
        if ("".equals(result)) {
            return default_;
        }
        return as_int(option);
    }

    public boolean as_bool(String option) throws Exception {
        String result = as_string(option);
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
            throw new Exception("SOSArguments.as_bool(): invalid parameter [" + option + "]: " + x.getMessage(), x);
        }
    }

    public boolean as_bool(String option, boolean default_) throws Exception {
        String result = as_string(option, "");
        if ("".equals(result)) {
            return default_;
        }
        return as_bool(option);
    }

    public void check_all_used() throws Exception {
        for (Iterator it = _arguments.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String option = (String) entry.getKey();
            Argument argument = (Argument) entry.getValue();
            if (!argument._read) {
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
            if ((arg.length() > equal + 1) && arg.charAt(equal + 1) == '\'' && arg.charAt(arg.length() - 1) == '\'') {
                arg = arg.substring(0, equal + 1) + arg.substring(equal + 2, arg.length() - 1);
            }
            _arguments.put(arg.substring(0, equal + 1), new Argument(arg.substring(equal + 1)));
        }
    }

    public static void main(String[] args) {
        try {
            SOSArguments arguments = new SOSArguments(args, true);
            java.util.Iterator keys = arguments._arguments.keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                System.out.println(key + arguments.as_string(key.toString()));
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public Map get_arguments() {
        return _arguments;
    }

}