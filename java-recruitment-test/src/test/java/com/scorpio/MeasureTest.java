package com.scorpio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasureTest {

    @Test
    void isMatchingName_simpleMatch() {
        Measure m = new Measure();
        m.setName("Heating System");
        assertTrue(m.isMatchingName("heating"));
    }

    @Test
    void isMatchingName_caseInsensitive() {
        Measure m = new Measure();
        m.setName("Temperature Sensor");
        assertTrue(m.isMatchingName("TEMPERATURE"));
        assertTrue(m.isMatchingName("temperature"));
        assertTrue(m.isMatchingName("Temperature"));
    }

    @Test
    void isMatchingName_partialMatch() {
        Measure m = new Measure();
        m.setName("Room Heating Control");
        assertTrue(m.isMatchingName("heating"));
        assertTrue(m.isMatchingName("room"));
        assertTrue(m.isMatchingName("control"));
    }

    @Test
    void isMatchingName_exactMatch() {
        Measure m = new Measure();
        m.setName("Sensor");
        assertTrue(m.isMatchingName("sensor"));
        assertTrue(m.isMatchingName("Sensor"));
    }

    @Test
    void isMatchingName_noMatch() {
        Measure m = new Measure();
        m.setName("Heating System");
        assertFalse(m.isMatchingName("cooling"));
        assertFalse(m.isMatchingName("pump"));
    }

    @Test
    void isMatchingName_nullWord() {
        Measure m = new Measure();
        m.setName("Sensor");
        assertFalse(m.isMatchingName(null));
    }

    @Test
    void isMatchingName_emptyWord() {
        Measure m = new Measure();
        m.setName("Sensor");
        assertFalse(m.isMatchingName(""));
    }

    @Test
    void isMatchingName_specialCharacters() {
        Measure m = new Measure();
        m.setName("Temp-Sensor_V2.0");
        assertTrue(m.isMatchingName("temp-sensor"));
        assertTrue(m.isMatchingName("v2"));
        assertTrue(m.isMatchingName("-"));
    }

    @Test
    void isMatchingName_whitespace() {
        Measure m = new Measure();
        m.setName("Room Heating Controller");
        assertTrue(m.isMatchingName("room heating"));
        assertTrue(m.isMatchingName("heating controller"));
    }

    @Test
    void isMatchingName_unicodeCharacters() {
        Measure m = new Measure();
        m.setName("Température Capteur");
        assertTrue(m.isMatchingName("température"));
        assertTrue(m.isMatchingName("capteur"));
    }
}
