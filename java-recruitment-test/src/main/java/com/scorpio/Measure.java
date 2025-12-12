package com.scorpio;

import lombok.Getter;
import lombok.Setter;

/**
 * Measure class representing a measurable entity with an ID, name, and data type.
 */
@Getter
@Setter
public class Measure {
    private long id;

    private String name;

    private DataType dataType;

    public Measure() {
    }

    public Measure(long id, String name, DataType dataType) {
        this();
        this.id = id;
        this.name = name;
        this.dataType = dataType;
    }

    /**
     * Checks if the measure name contains the given word (case insensitive).
     * 
     * @param word The word to search for
     * @return true if the measure name contains the word, false otherwise
     */
    public boolean isMatchingName(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        String measureName = this.getName();
        return measureName != null && measureName.toLowerCase().contains(word.toLowerCase());
    }
}
