package sos.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Andreas Püschel */
@Deprecated
public class SOSCommandline {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSCommandline.class);

    public static String[] splitArguments(final String arguments) throws Exception {
        String[] resultArguments = null;
        Vector resultVector = new Vector();
        int resultIndex = 0;
        String resultString = "";
        boolean inQuote = false;
        try {
            for (int i = 0; i < arguments.length(); i++) {
                if (arguments.substring(i).startsWith("\\\"")) {
                    int pos1 = i;
                    int pos2 = arguments.indexOf("\\\"", pos1 + 1);
                    if (pos2 > -1) {
                        resultString = arguments.substring(pos1, pos2 + 1);
                        resultVector.add(resultIndex++, resultString);
                        resultString = "";
                        i = pos2 + 1;
                        continue;
                    }
                }
                if (arguments.substring(i).startsWith("\'")) {
                    int pos1 = i;
                    int pos2 = arguments.indexOf("\'", pos1 + 1);
                    if (pos2 > -1) {
                        resultString = arguments.substring(pos1, pos2 + 1);
                        resultVector.add(resultIndex++, resultString);
                        resultString = "";
                        i = pos2 + 1;
                        continue;
                    }
                }
                if (inQuote) {
                    resultString += arguments.charAt(i);
                    continue;
                }
                if (arguments.charAt(i) != ' ') {
                    resultString += arguments.charAt(i);
                } else {
                    resultVector.add(resultIndex++, resultString);
                    resultString = "";
                }
            }
            if (!resultString.trim().isEmpty()) {
                resultVector.add(resultIndex++, resultString);
            }
            resultArguments = new String[resultIndex];
            resultVector.copyInto(resultArguments);
            return resultArguments;
        } catch (Exception e) {
            throw new Exception("error occurred splitting arguments: " + e.getMessage(), e);
        }
    }

    /** executes a command */
    public static Vector execute(final String command) {
        Vector returnValues = new Vector();
        try {
            try {
                Process p = Runtime.getRuntime().exec(SOSCommandline.splitArguments(command));
                final BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                final BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                p.waitFor();
                LOGGER.debug("command returned exit code: " + p.exitValue());
                returnValues.add(0, new Integer(p.exitValue()));
                try {
                    String line = "";
                    String stdout = "";
                    while (line != null) {
                        line = stdInput.readLine();
                        if (line != null) {
                            stdout += line;
                        }
                    }
                    try {
                        if (stdout != null && stdout.trim().length() > 0) {
                            LOGGER.trace("Command returned output to stdout ...");
                        } else {
                            LOGGER.trace("Command did not return any output to stdout.");
                        }
                    } catch (Exception exc) {
                        //
                    }
                    returnValues.add(1, stdout);
                } catch (Exception ex) {
                    returnValues.add(1, "");
                    returnValues.add(2, ex.getMessage());
                    LOGGER.debug("error occurred processing stdout: " + ex.getMessage());
                }

                try {
                    String line = "";
                    String stderr = "";
                    while (line != null) {
                        line = stdError.readLine();
                        if (line != null) {
                            stderr += line;
                        }
                    }
                    try {
                        if (stderr != null && stderr.trim().length() > 0) {
                            LOGGER.trace("Command returned output to stderr ...");
                        } else {
                            LOGGER.trace("Command did not return any output to stderr.");
                        }

                    } catch (Exception exc) {
                    }
                    returnValues.add(2, stderr);
                } catch (Exception ex) {
                    returnValues.add(2, ex.getMessage());
                    LOGGER.debug("error occurred processing stderr: " + ex.getMessage());
                }
                if (stdInput != null) {
                    stdInput.close();
                }
                if (stdError != null) {
                    stdError.close();
                }
            } catch (Exception ex) {
                returnValues.add(0, new Integer(1));
                returnValues.add(1, "");
                returnValues.add(2, ex.getMessage());
                LOGGER.debug("error occurred executing command: " + ex.getMessage());
            }
        } catch (Exception e) {
            LOGGER.debug("Command could not be executed successfully: " + e.getMessage());
            returnValues.add(0, new Integer(1));
            returnValues.add(1, "");
            returnValues.add(2, e.getMessage());
        }
        return returnValues;
    }

    public static String getExternalPassword(final String password) {
        String returnPassword = password;
        String firstChar = null;
        try {
            if (password != null && password.startsWith("`") && password.endsWith("`")) {
                String command = password.substring(1, password.length() - 1);
                LOGGER.debug("Trying to get password by executing command in backticks: " + command);
                Vector returnValues = SOSCommandline.execute(command);
                Integer exitValue = (Integer) returnValues.elementAt(0);
                if (exitValue.compareTo(new Integer(0)) == 0 && (String) returnValues.elementAt(1) != null) {
                    returnPassword = (String) returnValues.elementAt(1);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Using password string as password. Command could not be executed: " + e);
        }
        return returnPassword;
    }

    public static void main(final String[] args) {
        try {
            String password = "`c:/sosftp/getpassword.cmd \"a\" \"b\" `";
            password = "`\\tmp\\lGetPasswd.cmd ssh wilma.sos sos`";
            LOGGER.debug(getExternalPassword(password));
        } catch (Exception e) {
            LOGGER.error("error:" + e.toString());
        }

    }

}