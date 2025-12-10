package com.scorpio;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@AllArgsConstructor
public class Plc {

    public static void main(String[] args) throws IOException, IllegalArgumentException {

        ObjectMapper mapper = new ObjectMapper();
        try (InputStream stream = Plc.class.getResourceAsStream("/bacnetPlcTree.json")) {
            if (stream == null) {
                throw new IOException("Resource not found: /bacnetPlcTree.json");
            }
            Category root = mapper.readValue(stream, new TypeReference<Category>() {});

            // if an argument is provided, search for measures containing that word
            if (args != null && args.length > 1) {
                String searchType = args[0];
                String searchParam = args[1];
                List<String> found = new java.util.ArrayList<>();
                switch (searchType) {
                    case "id":
                        found = root.findMeasuresById(searchParam);
                        break;
                    case "name":
                        found = root.findMeasuresByName(searchParam);
                        break;
                    case "type":
                        found = root.findMeasuresByType(searchParam);
                        break;
                    default:
                        throw new IllegalArgumentException("Search type not supported: " + searchType);
                }
                if (found.isEmpty()) {
                    log.info("No measures found by {} containing : {}", searchType, searchParam);
                } else {
                    log.info("Measures found by {} containing {}:", searchType, searchParam);
                    for (String path : found) {
                        log.info(path);
                    }
                }
            }
            else throw new IllegalArgumentException("Please provide search arguments: <searchType> <searchParam> where searchType is one of [id, name, type]");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
