
package com.sos.joc.model.joe.common;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeployFailReasonType {

    WRONG_OWNERSHIP("WRONG_OWNERSHIP"),
    MISSING_OBJECT_PERMISSIONS("MISSING_OBJECT_PERMISSIONS"),
    MISSING_FOLDER_PERMISSIONS("MISSING_FOLDER_PERMISSIONS"),
    INCOMPLETE_CONFIGURATION("INCOMPLETE_CONFIGURATION");
    private final String value;
    private final static Map<String, DeployFailReasonType> CONSTANTS = new HashMap<String, DeployFailReasonType>();

    static {
        for (DeployFailReasonType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private DeployFailReasonType(String value) {
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
    public static DeployFailReasonType fromValue(String value) {
        DeployFailReasonType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
