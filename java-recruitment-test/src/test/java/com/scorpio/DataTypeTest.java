package com.scorpio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataTypeTest {

    @Test
    void fromString_upperCase() {
        DataType result = DataType.fromString("SHORT");
        assertEquals(DataType.SHORT, result);
    }

    @Test
    void fromString_lowerCase() {
        DataType result = DataType.fromString("short");
        assertEquals(DataType.SHORT, result);
    }

    @Test
    void fromString_mixedCase() {
        DataType result = DataType.fromString("Short");
        assertEquals(DataType.SHORT, result);
    }

    @Test
    void fromString_unknownType() {
        DataType result = DataType.fromString("UNKNOWN");
        assertNull(result);
    }

    @Test
    void fromString_emptyType() {
        DataType result = DataType.fromString("");
        assertNull(result);
    }

    @Test
    void fromString_nullType() {
        DataType result = DataType.fromString(null);
        assertNull(result);
    }

    @Test
    void fromString_invalidType() {
        DataType result = DataType.fromString("INVALID_TYPE");
        assertNull(result);
    }
}
