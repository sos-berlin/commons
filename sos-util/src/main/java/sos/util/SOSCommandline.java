package sos.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.log4j.Logger;

/** @author Andreas Püschel */
@Deprecated
public class SOSCommandline {

    private static final Logger LOGGER = Logger.getLogger(SOSCommandline.class);

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
        return SOSCommandline.execute(command, null);
    }

    /** executes a command */
    public static Vector execute(final String command, final SOSLogger logger) {
        Vector returnValues = new Vector();
        try {
            try {
                Process p = Runtime.getRuntime().exec(SOSCommandline.splitArguments(command));
                final BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                final BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                p.waitFor();
                if (logger != null) {
                    try {
                        logger.debug3("command returned exit code: " + p.exitValue());
                    } catch (Exception exc) {
                    }
                }
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
                        if (logger != null) {
                            if (stdout != null && stdout.trim().length() > 0) {
                                logger.debug8("Command returned output to stdout ...");
                            } else {
                                logger.debug8("Command did not return any output to stdout.");
                            }
                        }
                    } catch (Exception exc) {
                    }
                    returnValues.add(1, stdout);
                } catch (Exception ex) {
                    returnValues.add(1, "");
                    returnValues.add(2, ex.getMessage());
                    if (logger != null) {
                        try {
                            logger.debug("error occurred processing stdout: " + ex.getMessage());
                        } catch (Exception exc) {
                        }
                    }
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
                        if (logger != null) {
                            if (stderr != null && stderr.trim().length() > 0) {
                                logger.debug8("Command returned output to stderr ...");
                            } else {
                                logger.debug8("Command did not return any output to stderr.");
                            }
                        }
                    } catch (Exception exc) {
                    }
                    returnValues.add(2, stderr);
                } catch (Exception ex) {
                    returnValues.add(2, ex.getMessage());
                    if (logger != null) {
                        try {
                            logger.debug("error occurred processing stderr: " + ex.getMessage());
                        } catch (Exception exc) {
                        }
                    }
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
                if (logger != null) {
                    try {
                        logger.debug("error occurred executing command: " + ex.getMessage());
                    } catch (Exception exc) {
                    }
                }
            }
        } catch (Exception e) {
            try {
                if (logger != null) {
                    logger.debug("Command could not be executed successfully: " + e.getMessage());
                }
            } catch (Exception ex) {
            }
            returnValues.add(0, new Integer(1));
            returnValues.add(1, "");
            returnValues.add(2, e.getMessage());
        }
        return returnValues;
    }

    /** Checks if a command needs to be executed to get the password */
    public static String getExternalPassword(final String password) {
        return SOSCommandline.getExternalPassword(password, null);
    }

    /** Checks if an external command needs to be executed to get the password */
    public static String getExternalPassword(final String password, final SOSLogger logger) {
        String returnPassword = password;
        String firstChar = null;
        try {
            if (password != null && password.startsWith("`") && password.endsWith("`")) {
                String command = password.substring(1, password.length() - 1);
                try {
                    if (logger != null) {
                        logger.debug3("Trying to get password by executing command in backticks: " + command);
                    }
                } catch (Exception ex) {
                    LOGGER.error(ex);
                }
                Vector returnValues = SOSCommandline.execute(command, logger);
                Integer exitValue = (Integer) returnValues.elementAt(0);
                if (exitValue.compareTo(new Integer(0)) == 0 && (String) returnValues.elementAt(1) != null) {
                    returnPassword = (String) returnValues.elementAt(1);
                }
            }
        } catch (Exception e) {
            if (logger != null) {
                try {
                    logger.debug3("Using password string as password. Command could not be executed: " + e);
                } catch (Exception ex) {
                    LOGGER.error(ex);
                }
            }
        }
        return returnPassword;
    }

    public static void main(final String[] args) {
        try {
            SOSStandardLogger logger = new SOSStandardLogger(9);
            String password = "`c:/sosftp/getpassword.cmd \"a\" \"b\" `";
            password = "`\\tmp\\lGetPasswd.cmd ssh wilma.sos sos`";
            LOGGER.debug(getExternalPassword(password, logger));
        } catch (Exception e) {
            LOGGER.error("error:" + e.toString());
        }

    }
}
