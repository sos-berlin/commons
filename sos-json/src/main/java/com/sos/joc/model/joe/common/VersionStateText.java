
package com.sos.joc.model.joe.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Generated("org.jsonschema2pojo")
public enum VersionStateText {

    LIVE_IS_NEWER("LIVE_IS_NEWER"),
    DRAFT_IS_NEWER("DRAFT_IS_NEWER"),
    LIVE_NOT_EXIST("LIVE_NOT_EXIST");
    private final String value;
    private final static Map<String, VersionStateText> CONSTANTS = new HashMap<String, VersionStateText>();

    static {
        for (VersionStateText c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private VersionStateText(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static VersionStateText fromValue(String value) {
        VersionStateText constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
