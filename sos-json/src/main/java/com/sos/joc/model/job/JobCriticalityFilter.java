
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobCriticalityFilter {

    NORMAL("normal"),
    MINOR("minor"),
    MAJOR("major");
    private final String value;
    private final static Map<String, JobCriticalityFilter> CONSTANTS = new HashMap<String, JobCriticalityFilter>();

    static {
        for (JobCriticalityFilter c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private JobCriticalityFilter(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static JobCriticalityFilter fromValue(String value) {
        JobCriticalityFilter constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
