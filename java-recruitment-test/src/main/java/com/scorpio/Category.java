package com.scorpio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Category {
    private long id;

    private String name;

    private List<Category> categories;

    private List<Measure> measures;

    /**
     * Searches recursively for measures whose name contains the given word (case insensitive).
     * Returns a list of full paths where each path is composed of categories
     * separated by '/' followed by the measure name.
     * Example path format: "RootCategory/SubCategory/MeasureName"
     *
     * @param name name to search
     * @return list of paths of matching measures
     */
    public List<String> findMeasuresByName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is not defined");
        }
        return findMeasuresByName(name.toLowerCase(), null);
    }

    /**
     * Searches recursively for measures whose name contains the given word (case insensitive).
     *
     * @param word word to search
     * @param path parent category path
     * @return list of paths of matching measures
     */
    private List<String> findMeasuresByName(String word, Deque<String> path) {
        List<String> results = new ArrayList<>();

        // get current category path
        path = this.getCurrentPath(path);

        // search the word in this category's measures
        List<Measure> measures = this.getMeasures();
        if (measures != null) {
            for (Measure m : measures) {
                if (m.isMatchingName(word)) {
                    List<String> parts = new ArrayList<>(path);
                    parts.add(m.getName());
                    results.add(String.join("/", parts));
                }
            }
        }

        // search the word in child categories
        List<Category> children = this.getCategories();
        if (children != null) {
            for (Category child : children) {
                if (child == null) continue;
                List<String> childResults = child.findMeasuresByName(word, path);
                if (childResults != null && !childResults.isEmpty()) {
                    results.addAll(childResults);
                }
            }
        }
        
        // backtrack to the parent path
        path.removeLast();

        return results;
    }

    /**
     * Searches recursively for measures whose id equals to the given id.
     * Returns a list of full paths where each path is composed of categories separated by '/' followed by the measure name.
     * Example path format: "RootCategory/SubCategory/MeasureName"
     *
     * @param id id to search
     * @return list of paths of matching measures
     */
    public List<String> findMeasuresById(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id is not defined");
        }
        try {
            Long identifier = Long.valueOf(id);
            return findMeasuresById(identifier, null);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid id: " + id);
        }
    }

    /**
     * Searches recursively for measures whose id equals to the given id.
     *
     * @param id id to search
     * @param path parent category path
     * @return list of paths of matching measures
     */
    private List<String> findMeasuresById(Long id, Deque<String> path) {
        List<String> results = new ArrayList<>();

        // get current category path
        path = this.getCurrentPath(path);

        // search the word in this category's measures
        List<Measure> measures = this.getMeasures();
        if (measures != null) {
            for (Measure m : measures) {
                if (m.getId() == id) {
                    List<String> parts = new ArrayList<>(path);
                    parts.add(m.getName());
                    results.add(String.join("/", parts));
                }
            }
        }

        // search the word in child categories
        List<Category> children = this.getCategories();
        if (children != null) {
            for (Category child : children) {
                if (child == null) continue;
                List<String> childResults = child.findMeasuresById(id, path);
                if (childResults != null && !childResults.isEmpty()) {
                    results.addAll(childResults);
                }
            }
        }
        
        // backtrack to the parent path
        path.removeLast();

        return results;
    }

    /**
     * Searches recursively for measures whose type equals to the given type.
     * Returns a list of full paths where each path is composed of categories separated by '/' followed by the measure name.
     * Example path format: "RootCategory/SubCategory/MeasureName"
     *
     * @param type type to search
     * @return list of paths of matching measures
     * @throws IllegalArgumentException if the type is invalid
     */
    public List<String> findMeasuresByType(String type) throws IllegalArgumentException {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Data type is not defined");
        }
        DataType dt = DataType.fromString(type);
        if (dt == null) {
            throw new IllegalArgumentException("Invalid data type: " + type);
        }
        return findMeasuresByType(dt, null);
    }

    /**
     * Searches recursively for measures whose type equals to the given type.
     *
     * @param type type to search
     * @param path parent category path
     * @return list of paths of matching measures
     */
    private List<String> findMeasuresByType(DataType type, Deque<String> path) {
        List<String> results = new ArrayList<>();

        // get current category path
        path = this.getCurrentPath(path);

        // search the word in this category's measures
        List<Measure> measures = this.getMeasures();
        if (measures != null) {
            for (Measure m : measures) {
                if (m.getDataType() == type) {
                    List<String> parts = new ArrayList<>(path);
                    parts.add(m.getName());
                    results.add(String.join("/", parts));
                }
            }
        }

        // search the word in child categories
        List<Category> children = this.getCategories();
        if (children != null) {
            for (Category child : children) {
                if (child == null) continue;
                List<String> childResults = child.findMeasuresByType(type, path);
                if (childResults != null && !childResults.isEmpty()) {
                    results.addAll(childResults);
                }
            }
        }
        
        // backtrack to the parent path
        path.removeLast();

        return results;
    }

    /**
     * Gets the current category path from the parent path.
     * @param path The parent category path
     * @return the current category path
     */
    private Deque<String> getCurrentPath(Deque<String> path) {
        if (path == null) path = new LinkedList<>();
        if (this.getName() != null && !this.getName().isBlank()) {
            path.addLast(this.getName());
        } else {
            path.addLast("category-" + this.getId());
        }
        return path;
    }

}
