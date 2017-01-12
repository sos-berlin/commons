package sos.util;

import java.util.TimeZone;

import org.apache.log4j.EnhancedPatternLayout;


public class SOSLog4jEnhancedPatternLayout extends EnhancedPatternLayout {

    @Override
    public void setConversionPattern(String conversionPattern) {
        String defaultTimeZoneId = TimeZone.getDefault().getID();
        String conversionPatternModified = conversionPattern.replaceAll("\\{localTime\\}", "{" + defaultTimeZoneId + "}");
        super.setConversionPattern(conversionPatternModified);
    }
}