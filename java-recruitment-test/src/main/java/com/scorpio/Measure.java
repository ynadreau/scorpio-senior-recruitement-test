package com.scorpio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Measure {
    private long id;

    private String name;

    private DataType dataType;

    /**
     * Checks if the measure name contains the given word (case insensitive).
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
