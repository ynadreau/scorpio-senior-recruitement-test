package com.scorpio;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

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

    /**
     * Returns a predicate that tests if a Measure's name contains the given word (case insensitive).
     * @param word the word to search for
     * @return a predicate for Measure name matching
     */
    public static Predicate<Measure> nameContains(String word) {
        return m -> m.isMatchingName(word);
    }

    /**
     * Returns a predicate that tests if a Measure's ID equals the given ID.
     * @param idStr the ID string to match
     * @return a predicate for Measure ID matching
     * @throws IllegalArgumentException if idStr is not a valid long
     */
    public static Predicate<Measure> idEquals(String idStr) throws IllegalArgumentException {
        try {
            long id = Long.parseLong(idStr);
            return m -> m.getId() == id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid id: " + idStr);
        }
    }

    /**
     * Returns a predicate that tests if a Measure's data type matches the given type string.
     * @param typeStr the type string to match
     * @return a predicate for Measure data type matching
     */
    public static Predicate<Measure> typeEquals(String typeStr) {
        return m -> DataType.matches(typeStr).test(m.getDataType());
    }
}
