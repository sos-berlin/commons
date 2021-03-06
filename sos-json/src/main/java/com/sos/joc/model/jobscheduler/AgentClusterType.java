
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AgentClusterType {

    SINGLE_AGENT("SINGLE_AGENT"),
    FIX_PRIORITY("FIX_PRIORITY"),
    ROUND_ROBIN("ROUND_ROBIN");
    private final String value;
    private final static Map<String, AgentClusterType> CONSTANTS = new HashMap<String, AgentClusterType>();

    static {
        for (AgentClusterType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private AgentClusterType(String value) {
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
    public static AgentClusterType fromValue(String value) {
        AgentClusterType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
