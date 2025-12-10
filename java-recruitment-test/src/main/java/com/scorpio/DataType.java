package com.scorpio;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DataType {

    SHORT("Short", Short.class),

    INTEGER("Integer", Integer.class),

    LONG("Long", Long.class),

    FLOAT("Float", Float.class),

    DOUBLE("Double", Double.class),

    BOOLEAN("Boolean", Boolean.class),

    STRING("String", String.class);

    /**
     * Convert a string to a DataType.
     * Accepts either the enum name (case-insensitive) or the JSON value (case-insensitive),
     * e.g. "SHORT", "short", "Short".
     * Returns null if no matching DataType is found.
     */
    public static DataType fromString(String val) {
        if (val == null) return null;
        val = val.trim();
        for (DataType dataType : DataType.values()) {
            if (dataType.name().equalsIgnoreCase(val) || dataType.getValue().equalsIgnoreCase(val)) {
                return dataType;
            }
        }
        return null;
    }
    
    @JsonValue
    private final String value;

    private final Class<?> clazz;

    DataType(String value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }
}
