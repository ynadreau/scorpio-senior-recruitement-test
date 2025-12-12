package com.scorpio;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    void findMeasuresByName_simpleMatch() {
        Category root = new Category(1, "Root");
        Measure m = new Measure(10, "Heating Temp", DataType.FLOAT);
        root.setMeasures(List.of(m));

        List<String> results = root.findMeasuresByName("heating");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Root/Heating Temp", results.get(0));
    }

    @Test 
    void findMeasuresByName_multipleChildren() {
        Category root = new Category(1, "Root");
        Category floor1 = new Category(2, "Floor1");
        Measure m1 = new Measure(11, "Room Heating", DataType.FLOAT);
        floor1.setMeasures(List.of(m1));
        Category floor2 = new Category(3, "Floor2");
        Measure m2 = new Measure(12, "room heating sensor", DataType.FLOAT);
        floor2.setMeasures(List.of(m2));
        root.setCategories(List.of(floor1, floor2));

        List<String> results = root.findMeasuresByName("HEATing");
        assertNotNull(results);
        // ensure both matches present regardless of order
        assertEquals(2, results.size());
        assertTrue(results.contains("Root/Floor1/Room Heating"));
        assertTrue(results.contains("Root/Floor2/room heating sensor"));
    }

    @Test
    void findMeasuresByName_notfound() {
        Category root = new Category(1, "Root");

        List<String> results = root.findMeasuresByName("NameNotFound");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void findMeasuresByName_throwsException() {
        Category root = new Category(1, "Root");

        assertThrows(IllegalArgumentException.class, () -> root.findMeasuresByName(null));
    }

    @Test
    void findMeasuresById_simpleMatch() {
        Category root = new Category(1, "Root");
        Measure m = new Measure(10, "Temperature Sensor", DataType.FLOAT);
        m.setName("Temperature Sensor");
        root.setMeasures(List.of(m));

        List<String> results = root.findMeasuresById("10");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Root/Temperature Sensor", results.get(0));
    }

    @Test
    void findMeasuresById_multipleChildren() {
        Category root = new Category(1, "Root");
        Category cat1 = new Category(2, "Building A");
        Measure m1 = new Measure(100, "Temp A", DataType.FLOAT);
        cat1.setMeasures(List.of(m1));
        Category cat2 = new Category(3, "Building B");
        Measure m2 = new Measure(100, "Temp B", DataType.FLOAT);
        cat2.setMeasures(List.of(m2));
        root.setCategories(List.of(cat1, cat2));

        List<String> results = root.findMeasuresById("100");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.contains("Root/Building A/Temp A"));
        assertTrue(results.contains("Root/Building B/Temp B"));
    }

    @Test
    void findMeasuresById_notFound() {
        Category root = new Category(1, "Root");
        Measure m = new Measure(10, "Sensor", DataType.SHORT);
        root.setMeasures(List.of(m));

        List<String> results = root.findMeasuresById("999");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void findMeasuresById_throwsException() {
        Category root = new Category(1, "Root");

        assertThrows(IllegalArgumentException.class, () -> root.findMeasuresById(null));
        assertThrows(IllegalArgumentException.class, () -> root.findMeasuresById("not_a_number"));
    }

    @Test
    void findMeasuresByType_simpleMatch() {
        Category root = new Category(1, "Root");
        Measure m = new Measure(10, "Pressure Reading", DataType.FLOAT);
        root.setMeasures(List.of(m));

        List<String> results = root.findMeasuresByType("FLOAT");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Root/Pressure Reading", results.get(0));
    }

    @Test
    void findMeasuresByType_caseInsensitive() {
        Category root = new Category(1, "Root");
        Measure m1 = new Measure(10, "Count", DataType.INTEGER);
        Measure m2 = new Measure(11, "Value", DataType.INTEGER);
        root.setMeasures(List.of(m1, m2));

        // Test various cases
        List<String> resultsUpper = root.findMeasuresByType("INTEGER");
        List<String> resultsLower = root.findMeasuresByType("integer");
        List<String> resultsMixed = root.findMeasuresByType("Integer");

        assertEquals(2, resultsUpper.size());
        assertEquals(2, resultsLower.size());
        assertEquals(2, resultsMixed.size());
    }

    @Test
    void findMeasuresByType_throwsException() {
        Category root = new Category(1, "Root");
        Measure m = new Measure(10, "Sensor", DataType.SHORT);
        root.setMeasures(List.of(m));

        assertThrows(IllegalArgumentException.class, () -> root.findMeasuresByType(null));
        assertThrows(IllegalArgumentException.class, () -> root.findMeasuresByType(""));
        assertThrows(IllegalArgumentException.class, () -> root.findMeasuresByType("unknown_type"));
    }

    @Test
    void findMeasuresByType_multipleChildren() {
        Category root = new Category(1, "Root");
        Category sensors = new Category(2, "Sensors");
        Measure temp = new Measure(10, "Temperature", DataType.FLOAT);
        Measure pressure = new Measure(11, "Pressure", DataType.DOUBLE);
        sensors.setMeasures(List.of(temp, pressure));
        Category switches = new Category(3, "Switches");
        Measure alarm = new Measure(13, "Alarm", DataType.BOOLEAN);
        Measure counter = new Measure(14, "Counter", DataType.LONG);
        switches.setMeasures(List.of(alarm, counter));
        root.setCategories(List.of(sensors, switches));

        // Search for FLOAT type
        List<String> floatResults = root.findMeasuresByType("FLOAT");
        assertEquals(1, floatResults.size());
        assertTrue(floatResults.contains("Root/Sensors/Temperature"));

        // Search for BOOLEAN type
        List<String> boolResults = root.findMeasuresByType("BOOLEAN");
        assertEquals(1, boolResults.size());
        assertTrue(boolResults.contains("Root/Switches/Alarm"));
    }
}
