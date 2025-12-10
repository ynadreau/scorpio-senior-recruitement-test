package com.scorpio;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    void findMeasuresByName_simpleMatch() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Measure m = new Measure();
        m.setId(10);
        m.setName("Heating Temp");

        root.setMeasures(List.of(m));

        List<String> results = root.findMeasuresByName("heating");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Root/Heating Temp", results.get(0));
    }

    @Test 
    void findMeasuresByName_childrenAndCaseInsensitive() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Category floor1 = new Category();
        floor1.setId(2);
        floor1.setName("Floor1");
        Measure m1 = new Measure();
        m1.setId(11);
        m1.setName("Room Heating");
        floor1.setMeasures(List.of(m1));

        Category floor2 = new Category();
        floor2.setId(3);
        floor2.setName("Floor2");
        Measure m2 = new Measure();
        m2.setId(12);
        m2.setName("room heating sensor");
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
    void findMeasuresByName_emptyWord_returnsEmpty() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        List<String> results = root.findMeasuresByName("NameNotFound");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void findMeasuresById_simpleMatch() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Measure m = new Measure();
        m.setId(10);
        m.setName("Temperature Sensor");

        root.setMeasures(List.of(m));

        List<String> results = root.findMeasuresById("10");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Root/Temperature Sensor", results.get(0));
    }

    @Test
    void findMeasuresById_multipleChildren() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Category cat1 = new Category();
        cat1.setId(2);
        cat1.setName("Building A");
        Measure m1 = new Measure();
        m1.setId(100);
        m1.setName("Temp A");
        cat1.setMeasures(List.of(m1));

        Category cat2 = new Category();
        cat2.setId(3);
        cat2.setName("Building B");
        Measure m2 = new Measure();
        m2.setId(100);
        m2.setName("Temp B");
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
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Measure m = new Measure();
        m.setId(10);
        m.setName("Sensor");
        root.setMeasures(List.of(m));

        List<String> results = root.findMeasuresById("999");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void findMeasuresById_nullId_throwsException() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        assertThrows(IllegalArgumentException.class, () -> root.findMeasuresById(null));
    }

    @Test
    void findMeasuresById_invalidIdFormat_throwsException() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        assertThrows(IllegalArgumentException.class, () -> root.findMeasuresById("not_a_number"));
    }

    @Test
    void findMeasuresByType_simpleMatch() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Measure m = new Measure();
        m.setId(10);
        m.setName("Pressure Reading");
        m.setDataType(DataType.FLOAT);

        root.setMeasures(List.of(m));

        List<String> results = root.findMeasuresByType("FLOAT");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Root/Pressure Reading", results.get(0));
    }

    @Test
    void findMeasuresByType_caseInsensitive() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Measure m1 = new Measure();
        m1.setId(10);
        m1.setName("Count");
        m1.setDataType(DataType.INTEGER);
        
        Measure m2 = new Measure();
        m2.setId(11);
        m2.setName("Value");
        m2.setDataType(DataType.INTEGER);

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
    void findMeasuresByType_unknownType_throwsException() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Measure m = new Measure();
        m.setId(10);
        m.setName("Sensor");
        m.setDataType(DataType.SHORT);
        root.setMeasures(List.of(m));

        assertThrows(IllegalArgumentException.class, () -> root.findMeasuresByType("unknown_type"));
    }

    @Test
    void findMeasuresByType_multipleChildrenDifferentTypes() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Category sensors = new Category();
        sensors.setId(2);
        sensors.setName("Sensors");
        
        Measure temp = new Measure();
        temp.setId(10);
        temp.setName("Temperature");
        temp.setDataType(DataType.FLOAT);
        
        Measure pressure = new Measure();
        pressure.setId(11);
        pressure.setName("Pressure");
        pressure.setDataType(DataType.DOUBLE);
        
        sensors.setMeasures(List.of(temp, pressure));

        Category switches = new Category();
        switches.setId(3);
        switches.setName("Switches");
        
        Measure alarm = new Measure();
        alarm.setId(12);
        alarm.setName("Alarm");
        alarm.setDataType(DataType.BOOLEAN);
        
        Measure counter = new Measure();
        counter.setId(13);
        counter.setName("Counter");
        counter.setDataType(DataType.LONG);
        
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
