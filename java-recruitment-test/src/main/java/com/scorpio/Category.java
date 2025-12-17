package com.scorpio;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Category class representing a category that can contain sub-categories and measures.
 */
@Getter
@Setter
@JsonDeserialize(using = Category.CategoryDeserializer.class)
public class Category {
    private long id;

    private String name;

    private List<Category> categories;

    private List<Measure> measures;

    @JsonIgnore
    private Category parent;

    public Category() {
    }

    public Category(long id, String name) {
        this();
        this.id = id;
        this.name = name;
    }
    /**
     * Searches recursively for measures whose name contains the given word (case insensitive).
     * Returns a list of full paths where each path is composed of categories
     * separated by '/' followed by the measure name.
     * Example path format: "RootCategory/SubCategory/MeasureName"
     *
     * @param name name to search
     * @return list of paths of matching measures
     * @throws IllegalArgumentException
     */
    public List<String> findMeasuresByName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is not defined");
        }
        return findMeasuresRecursive(Measure.nameContains(name));
    }

    /**
     * Searches recursively for measures whose id equals to the given id.
     * Returns a list of full paths where each path is composed of categories separated by '/' followed by the measure name.
     * Example path format: "RootCategory/SubCategory/MeasureName"
     *
     * @param id id to search
     * @return list of paths of matching measures
     * @throws IllegalArgumentException
     */
    public List<String> findMeasuresById(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id is not defined");
        }
        return findMeasuresRecursive(Measure.idEquals(id));
    }

    /**
     * Searches recursively for measures whose type equals to the given type.
     * Returns a list of full paths where each path is composed of categories separated by '/' followed by the measure name.
     * Example path format: "RootCategory/SubCategory/MeasureName"
     *
     * @param type type to search
     * @return list of paths of matching measures
     * @throws IllegalArgumentException
     */
    public List<String> findMeasuresByType(String type) throws IllegalArgumentException {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Data type is not defined");
        }
        return findMeasuresRecursive(Measure.typeEquals(type));
    }

    /**
     * Searches recursively for measures matching the given predicate.
     */
    private List<String> findMeasuresRecursive(Predicate<Measure> predicate) {
        List<String> results = new ArrayList<>();

        // Get the full path to this category
        Deque<String> path = this.getPath();

        // Search in this category's measures
        List<Measure> measures = this.getMeasures();
        if (measures != null) {
            for (Measure m : measures) {
                if (predicate.test(m)) {
                    List<String> parts = new ArrayList<>(path);
                    parts.add(m.getName());
                    results.add(String.join("/", parts));
                }
            }
        }

        // Search in child categories
        List<Category> children = this.getCategories();
        if (children != null) {
            for (Category child : children) {
                if (child == null) continue;
                List<String> childResults = child.findMeasuresRecursive(predicate);
                if (childResults != null && !childResults.isEmpty()) {
                    results.addAll(childResults);
                }
            }
        }

        return results;
    }

    /**
     * Builds the full path from root to this category dynamically using parent references.
     * @return Deque containing the path segments from root to this category
     */
    public Deque<String> getPath() {
        Deque<String> path = new LinkedList<>();
        Category current = this;
        while (current != null) {
            String segment = (current.getName() != null && !current.getName().isBlank()) 
                ? current.getName() 
                : "category-" + current.getId();
            path.addFirst(segment);
            current = current.getParent();
        }
        return path;
    }

    /**
     * Custom deserializer for Category that sets parent references during deserialization.
     */
    public static class CategoryDeserializer extends JsonDeserializer<Category> {

        @Override
        public Category deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            ObjectMapper mapper = (ObjectMapper) p.getCodec();

            Category category = new Category(
                node.get("id").asLong(), 
                node.get("name").asText()
            );

            // Deserialize measures
            if (node.has("measures") && node.get("measures").isArray()) {
                ArrayNode measuresNode = (ArrayNode) node.get("measures");
                List<Measure> measures = new ArrayList<>();
                for (JsonNode measureNode : measuresNode) {
                    Measure measure = mapper.treeToValue(measureNode, Measure.class);
                    measures.add(measure);
                }
                category.setMeasures(measures);
            }

            // Deserialize categories recursively
            if (node.has("categories") && node.get("categories").isArray()) {
                ArrayNode categoriesNode = (ArrayNode) node.get("categories");
                List<Category> categories = new ArrayList<>();
                for (JsonNode categoryNode : categoriesNode) {
                    Category child = mapper.treeToValue(categoryNode, Category.class);
                    child.setParent(category);
                    categories.add(child);
                }
                category.setCategories(categories);
            }

            return category;
        }
    }

}
