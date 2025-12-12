package com.scorpio;

import lombok.Getter;

/**
 * Enumeration of supported search types.
 */
 @Getter
 public enum SearchType {
    /**
     * Search by measure name (contains - case insensitive).
     */
    NAME("name", "Search by measure name (contains)"),

    /**
     * Search by measure ID (exact match).
     */
    ID("id", "Search by measure ID (exact match)"),

    /**
     * Search by DataType (e.g. FLOAT, INTEGER, BOOLEAN, ... - case insensitive).
     */
    TYPE("type", "Search by DataType (FLOAT, INTEGER, etc.)");

    /**
     * Find a SearchType by its string value (case insensitive).
     *
     * @param value the string value to search for
     * @return the corresponding SearchType, or null if not found
     */
    public static SearchType fromString(String value) {
        if (value == null) {
            return null;
        }
        for (SearchType type : SearchType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get all valid search type values as a pipe-separated string.
     *
     * @return a string containing all valid search types separated by '|'
     */
    public static String getSupportedTypes() {
        StringBuilder sb = new StringBuilder();
        SearchType[] types = SearchType.values();
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                sb.append(" | ");
            }
            sb.append(types[i].value);
        }
        return sb.toString();
    }

    private final String value;
    private final String description;

    SearchType(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
