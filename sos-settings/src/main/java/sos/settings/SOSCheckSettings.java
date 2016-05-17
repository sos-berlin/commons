package sos.settings;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSString;

/** @author Mürüvet Öksüz */
public class SOSCheckSettings {

    private String source = "";
    private SOSLogger logger = null;
    private Properties sectionMandatory = null;
    private SOSString sosString = new SOSString();
    private String condition = "";
    private HashMap values = null;
    private HashMap rules = new HashMap();
    private String operator = " and | or | xor ";

    public SOSCheckSettings(Properties sectionMandatory, HashMap values, SOSLogger logger) throws Exception {
        try {
            this.sectionMandatory = sectionMandatory;
            this.values = values;
            this.logger = logger;
            this.condition = sosString.parseToString(sectionMandatory, "mandatory");
            init();
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public SOSCheckSettings(String condition, HashMap values, SOSLogger logger) throws Exception {
        try {
            this.values = values;
            this.logger = logger;
            this.condition = condition;
            init();
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public SOSCheckSettings(String condition, SOSLogger logger) throws Exception {
        try {
            this.condition = condition;
            this.logger = logger;
            values = new HashMap();
            values.put("true", "1");
            values.put("1", "1");
            init();
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private void init() throws Exception {
        try {
            rules = new HashMap();
            if ((condition.toLowerCase().indexOf(" or ") > -1) || (condition.toLowerCase().indexOf("(or") > -1)
                    || (condition.toLowerCase().indexOf(")or") > -1) || (condition.toLowerCase().indexOf(" or(") > -1)
                    || (condition.toLowerCase().indexOf(" or)") > -1)) {
                rules.put("0or0", "0");
                rules.put("0or1", "1");
                rules.put("1or0", "1");
                rules.put("1or1", "1");
                rules.put("\\(0or0\\)", "0");
                rules.put("\\(0or1\\)", "1");
                rules.put("\\(1or0\\)", "1");
                rules.put("\\(1or1\\)", "1");
            }
            if ((condition.toLowerCase().indexOf(" and ") > -1) || (condition.toLowerCase().indexOf("(and") > -1)
                    || (condition.toLowerCase().indexOf(")and") > -1) || (condition.toLowerCase().indexOf("and(") > -1)
                    || (condition.toLowerCase().indexOf("and)") > -1)) {
                rules.put("0and0", "0");
                rules.put("0and1", "0");
                rules.put("1and0", "0");
                rules.put("1and1", "1");
                rules.put("\\(0and0\\)", "0");
                rules.put("\\(0and1\\)", "0");
                rules.put("\\(1and0\\)", "0");
                rules.put("\\(1and1\\)", "1");
            }
            if ((condition.toLowerCase().indexOf(" xor ") > -1) || (condition.toLowerCase().indexOf("(xor") > -1)
                    || (condition.toLowerCase().indexOf(")xor") > -1) || (condition.toLowerCase().indexOf("xor(") > -1)
                    || (condition.toLowerCase().indexOf("xor)") > -1)) {
                rules.put("0xor0", "0");
                rules.put("0xor1", "1");
                rules.put("1xor0", "1");
                rules.put("1xor1", "0");
                rules.put("\\(0xor0\\)", "0");
                rules.put("\\(0xor1\\)", "1");
                rules.put("\\(1xor0\\)", "1");
                rules.put("\\(1xor1\\)", "0");
            }
            rules.put("\\(1\\)", "1");
            rules.put("\\(0\\)", "0");
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public boolean check(String element) throws Exception {
        try {
            return !sosString.parseToString(values, element).isEmpty();
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public String getCondition() throws Exception {
        try {
            return condition;
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }

    }

    public void setConditionValues(HashMap values) throws Exception {
        try {
            this.values = values;
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private String normalizedString(String key) throws Exception {
        try {
            key = key.replaceAll("\\(", "");
            key = key.replaceAll("\\)", "");
            key = key.replaceAll("  ", " ");
            return key;
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public boolean process() throws Exception {
        boolean retVal = false;
        String hCond = "";
        String[] cond = null;
        String msg = "";
        try {
            checkCondition();
            checkValues();
            hCond = condition;
            cond = normalizedString(hCond).split(operator);
            cond = this.sortDesc(cond, false);
            for (int i = 0; i < cond.length; i++) {
                if (sosString.parseToString(values, cond[i].trim()).isEmpty()) {
                    hCond = hCond.replaceAll(cond[i].trim(), "0");
                } else {
                    hCond = hCond.replaceAll(cond[i].trim(), "1");
                }
            }
            logger.debug("..Condition: " + condition);
            logger.debug("..is equal : " + hCond);
            hCond = hCond.trim();
            hCond = hCond.replaceAll(" ", "");
            while (hCond.length() != 1) {
                Iterator keys = rules.keySet().iterator();
                Iterator vals = rules.values().iterator();
                String key = "";
                String val = "";
                while (keys.hasNext() && hCond.length() != 1) {
                    key = keys.next().toString().trim();
                    val = vals.next().toString().trim();
                    msg = ".. put rules [" + key + "=" + val + "] to condition = " + hCond;
                    hCond = hCond.replaceAll("  ", " ").replaceAll(key, val);
                }
            }
            retVal = "1".equalsIgnoreCase(hCond);
            logger.debug("..result is : " + retVal);
            return retVal;
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public void getSection() throws Exception {
        SOSProfileSettings settings = null;
        try {
            logger.debug4("reading settings ..." + source);
            settings = new SOSProfileSettings(source, logger);
            sectionMandatory = settings.getSection("mandatory");
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private String[] sortDesc(String[] a, boolean desc) throws Exception {
        String[] aa = null;
        int len = -1;
        try {
            for (int i = 0; i < a.length - 1; i++) {
                for (int j = i + 1; j < a.length; j++) {
                    if (a[i].compareTo(a[j]) > 0) {
                        String temp = a[j];
                        a[j] = a[i];
                        a[i] = temp;
                    }
                }
            }
            if (desc) {
                return a;
            } else {
                len = a.length;
                aa = new String[len];
                for (int i = 0; i < len; i++) {
                    aa[len - 1 - i] = a[i];
                }
                return aa;
            }
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private void checkCondition() throws Exception {
        String hcon = null;
        try {
            condition = condition.toLowerCase();
            hcon = condition;
            if (condition != null && !condition.isEmpty()) {
                hcon = hcon.replaceAll("\\(", " \\( ");
                hcon = hcon.replaceAll("\\)", " \\) ");
                if (hcon.split("\\(").length != hcon.split("\\)").length) {
                    throw new Exception("\n ..count of \"(\" is not equal of count of \")\" for condition : " + condition);
                }
            }
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private void checkValues() throws Exception {
        HashMap hValues = new HashMap();
        try {
            Iterator keys = values.keySet().iterator();
            Iterator vals = values.values().iterator();
            String key = "";
            Object val = "";
            while (keys.hasNext()) {
                key = keys.next().toString().toLowerCase();
                val = vals.next();
                logger.debug5(".. " + key + "=" + val);
                hValues.put(key, val);
            }
            if (values == null) {
                values = new HashMap();
            } else {
                values.clear();
            }
            values.putAll(hValues);
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public static void main(String[] args) {
        HashMap values4condition = null;
        SOSCheckSettings checkSettings = null;
        sos.util.SOSStandardLogger sosLogger = null;
        try {
            sosLogger = new sos.util.SOSStandardLogger(9);
            values4condition = new HashMap();
            values4condition.put("vorname", "Hans");
            values4condition.put("name", "ss");
            values4condition.put("adresse", "Giesebrechtstr. 14");
            values4condition.put("kundennummer", "12345678");
            checkSettings = new SOSCheckSettings("(name and vorname) and kundennummer", values4condition, sosLogger);
            if (checkSettings.process()) {
                sosLogger.debug4("boolischer Ausdruck ist OK");
            } else {
                sosLogger.debug4("boolischer Ausdruck ist nicht OK");
            }
            sosLogger.debug4("..checked mandatory fields: " + checkSettings.getCondition());
        } catch (Exception e) {
            System.err.print("\n -> ..error occurred in SOSCheckSettings " + ": " + e);
        }
    }

}