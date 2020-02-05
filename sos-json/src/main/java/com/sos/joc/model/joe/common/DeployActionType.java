
package com.sos.joc.model.joe.common;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeployActionType {

    DEPLOYED("DEPLOYED"),
    DELETED("DELETED"),
    SKIPPED("SKIPPED");
    private final String value;
    private final static Map<String, DeployActionType> CONSTANTS = new HashMap<String, DeployActionType>();

    static {
        for (DeployActionType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private DeployActionType(String value) {
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
    public static DeployActionType fromValue(String value) {
        DeployActionType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
