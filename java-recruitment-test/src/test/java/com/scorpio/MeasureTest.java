package com.scorpio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasureTest {

    @Test
    void isMatchingName_simpleMatch() {
        Measure m = new Measure(1, "Heating System", DataType.FLOAT);
        assertTrue(m.isMatchingName("heating"));
    }

    @Test
    void isMatchingName_caseInsensitive() {
        Measure m = new Measure(1, "Temperature Sensor", DataType.FLOAT);
        assertTrue(m.isMatchingName("TEMPERATURE"));
        assertTrue(m.isMatchingName("temperature"));
        assertTrue(m.isMatchingName("Temperature"));
    }

    @Test
    void isMatchingName_partialMatch() {
        Measure m = new Measure(1, "Room Heating Control", DataType.FLOAT);
        assertTrue(m.isMatchingName("heating"));
        assertTrue(m.isMatchingName("room"));
        assertTrue(m.isMatchingName("control"));
    }

    @Test
    void isMatchingName_exactMatch() {
        Measure m = new Measure(1, "Sensor", DataType.FLOAT);
        assertTrue(m.isMatchingName("sensor"));
        assertTrue(m.isMatchingName("Sensor"));
    }

    @Test
    void isMatchingName_noMatch() {
        Measure m = new Measure(1, "Temperature Sensor", DataType.FLOAT);
        assertFalse(m.isMatchingName("cooling"));
        assertFalse(m.isMatchingName("pump"));
    }

    @Test
    void isMatchingName_nullWord() {
        Measure m = new Measure(1, "Sensor", DataType.FLOAT);
        assertFalse(m.isMatchingName(null));
    }

    @Test
    void isMatchingName_emptyWord() {
        Measure m = new Measure(1, "Sensor", DataType.FLOAT);
        assertFalse(m.isMatchingName(""));
    }

    @Test
    void isMatchingName_specialCharacters() {
        Measure m = new Measure(1, "Temp-Sensor_V2.0", DataType.FLOAT);
        assertTrue(m.isMatchingName("temp-sensor"));
        assertTrue(m.isMatchingName("v2"));
        assertTrue(m.isMatchingName("-"));
    }

    @Test
    void isMatchingName_whitespace() {
        Measure m = new Measure(1, "Room Heating Controller", DataType.FLOAT);
        assertTrue(m.isMatchingName("room heating"));
        assertTrue(m.isMatchingName("heating controller"));
    }

    @Test
    void isMatchingName_unicodeCharacters() {
        Measure m = new Measure(1, "Température Capteur", DataType.FLOAT);
        assertTrue(m.isMatchingName("température"));
        assertTrue(m.isMatchingName("capteur"));
    }
}
