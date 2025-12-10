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

        List<String> results = root.findMeasuresByName("");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void findMeasuresByName_categoryWithoutName_usesIdPlaceholder() {
        Category root = new Category();
        root.setId(1);
        root.setName("Root");

        Category unnamed = new Category();
        unnamed.setId(99);
        // name left null intentionally
        Measure m = new Measure();
        m.setId(20);
        m.setName("Boiler");
        unnamed.setMeasures(List.of(m));

        root.setCategories(List.of(unnamed));

        List<String> results = root.findMeasuresByName("boiler");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Root/category-99/Boiler", results.get(0));
    }
}
