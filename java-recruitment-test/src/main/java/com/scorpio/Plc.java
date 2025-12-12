package com.scorpio;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Main entry point for the SCORPIO PLC Measure Search Tool.
 * Supports both command-line and interactive modes for searching measures
 * by name, id, or DataType.
 */
@Slf4j
@AllArgsConstructor
public class Plc {

    // ==================== Constants ====================
    private static final String RESOURCE_PATH = "/bacnetPlcTree.json";
    private static final String RESOURCE_NOT_FOUND_ERROR = "Resource not found: " + RESOURCE_PATH;
    
    private static final String SEARCH_TYPE_PROMPT = "\nEnter search type (%s) or 'quit' (q) to exit: ";
    private static final String SEARCH_CRITERIA_PROMPT = "Enter search criteria: ";
    private static final String INVALID_SEARCH_TYPE_MESSAGE = "Invalid search type. Supported types: %s";
    private static final String EMPTY_SEARCH_CRITERIA_MESSAGE = "Search criteria cannot be empty.";
    private static final String NO_RESULTS_MESSAGE = "No measurement found by %s matching: '%s'";
    private static final String RESULTS_HEADER = "Measurements found by %s matching '%s':";
    private static final String RESULT_COUNT = "(%d result%s)";
    private static final String EXIT_MESSAGE = "Goodbye!";

    private static final String INTERACTIVE_MODE_USAGE = """
            
            ╔════════════════════════════════════════════════════════════╗
            ║           SCORPIO - PLC MEASURE SEARCH TOOL                ║
            ║                                                            ║
            ║  Supported Search Types:                                   ║
            ║    name  - Search by measure name (contains)               ║
            ║    id    - Search by measure ID (exact match)              ║
            ║    type  - Search by DataType (FLOAT, INTEGER, ...)        ║
            ╚════════════════════════════════════════════════════════════╝
            """;

    // ANSI color codes for console output
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    // Print error message
    private static void printError(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    // Print warning message
    private static void printWarning(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }

    // Print success message
    private static void printSuccess(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    // ==================== Main Entry Point ====================
    public static void main(String[] args) throws IOException {
        // Load PLC category tree from JSON resource
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream stream = Plc.class.getResourceAsStream(RESOURCE_PATH)) {
            if (stream == null) {
                throw new IOException(RESOURCE_NOT_FOUND_ERROR);
            }
            Category root = mapper.readValue(stream, new TypeReference<Category>() {});

            // Define mode: command-line or interactive
            if (args != null && args.length >= 2) {
                commandLineSearch(root, args[0], args[1]);
            } else {
                interactiveSearch(root);
            }

        } catch (Exception e) {
            printError(e.getMessage());
            throw e;
        }
    }

    /**
     * Command-line mode: process search from command-line arguments.
     */
    private static void commandLineSearch(Category root, String searchTypeStr, String searchParam) {
        SearchType searchType = SearchType.fromString(searchTypeStr);
        if (searchType == null || searchTypeStr.isBlank()) {
            printError(String.format(INVALID_SEARCH_TYPE_MESSAGE, SearchType.getSupportedTypes()));
            return;
        }
        if (searchParam == null || searchParam.isBlank()) {
            printError(EMPTY_SEARCH_CRITERIA_MESSAGE);
            return;
        }
        try {
            List<String> found = performSearch(root, searchType, searchParam);
            displayResult(found, searchType, searchParam);
        } catch (IllegalArgumentException e) {
            printError(e.getMessage());
        }
    }

    /**
     * Interactive mode: repeatedly prompt user for search type and criteria.
     */
    private static void interactiveSearch(Category root) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(INTERACTIVE_MODE_USAGE);
        try {
            while (true) {
                System.out.print(String.format(SEARCH_TYPE_PROMPT, SearchType.getSupportedTypes()));
                String input = scanner.nextLine().trim().toLowerCase();
                
                // Handle exit command
                if ("quit".equals(input) || "q".equals(input)) {
                    System.out.println(EXIT_MESSAGE);
                    break;
                }
                
                // Validate and parse search type
                SearchType searchType = SearchType.fromString(input);
                if (searchType == null || input.isBlank()) {
                    printError(String.format(INVALID_SEARCH_TYPE_MESSAGE, SearchType.getSupportedTypes()));
                    continue;
                }
                
                // Prompt for search criteria
                System.out.print(SEARCH_CRITERIA_PROMPT);
                String searchParam = scanner.nextLine().trim();
                
                if (searchParam.isEmpty()) {
                    printError(EMPTY_SEARCH_CRITERIA_MESSAGE);
                    continue;
                }
                
                // Perform and display search
                try {
                    List<String> found = performSearch(root, searchType, searchParam);
                    displayResult(found, searchType, searchParam);
                } catch (IllegalArgumentException e) {
                    printError(e.getMessage());
                }
            }    
        } finally {
            scanner.close();
        }
    }

    /**
     * Perform search based on type and parameter.
     */
    private static List<String> performSearch(Category root, SearchType searchType, String searchParam) throws IllegalArgumentException {
        return switch (searchType) {
            case NAME -> root.findMeasuresByName(searchParam);
            case ID -> root.findMeasuresById(searchParam);
            case TYPE -> root.findMeasuresByType(searchParam);
        };
    }

    /**
     * Display search results.
     */
    private static void displayResult(List<String> result, SearchType searchType, String searchParam) {
        if (result.isEmpty()) {
            printWarning(String.format(NO_RESULTS_MESSAGE, searchType.getValue(), searchParam));
        } else {
            printSuccess(String.format(RESULTS_HEADER, searchType.getValue(), searchParam));
            for (int i = 0; i < result.size(); i++) {
                printSuccess("  [" + (i + 1) + "] " + result.get(i));
            }
            String plural = result.size() > 1 ? "s" : "";
            printSuccess(String.format(RESULT_COUNT, result.size(), plural));
        }
    }
}
