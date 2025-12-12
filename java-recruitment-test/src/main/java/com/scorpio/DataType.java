package com.scorpio;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Enumeration of supported data types.
 */
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
     * 
     * @param type type to convert
     * @return the found data type, null otherwise
     */
    public static DataType fromString(String type) {
        if (type != null) {
            type = type.trim();
            for (DataType dataType : DataType.values()) {
                if (dataType.name().equalsIgnoreCase(type) || dataType.getValue().equalsIgnoreCase(type)) {
                    return dataType;
                }
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
