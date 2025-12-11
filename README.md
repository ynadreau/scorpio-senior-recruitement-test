# SCORPIO PLC Measure Search Tool

> A comprehensive Java application for searching and managing hierarchical PLC (Programmable Logic Controller) measure data from BACnet automation systems.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Usage](#usage)
- [Development](#development)
- [Testing](#testing)

## 🎯 Overview

The SCORPIO PLC Measure Search Tool is a Java application designed to load, parse, and efficiently search hierarchical measurement data from BACnet automation systems. The tool supports searching measures by name, ID, and DataType with full path tracking.

### Data Model

The BACnet tree is organized hierarchically:

```
Root Category
├── Category (e.g., "PAC-1")
│   ├── Sub-Category (e.g., "analogic-value")
│   │   ├── Measure (e.g., "Heating setpoint 1")
│   │   │   ├── ID: 1001
│   │   │   ├── DataType: FLOAT
│   │   ├── Measure (e.g., "Cooling setpoint")
│   │   │   ├── ID: 1002
│   │   │   ├── DataType: INTEGER
│   └── Sub-Category (e.g., "digital-value")
│       └── Measure (e.g., "System Status")
│           ├── ID: 2001
│           ├── DataType: BOOLEAN
```

## Features

**Hierarchical Data Management**
- Load BACnet tree from JSON (`bacnetPlcTree.json`)
- Navigate nested category structures
- Maintain complete path information for each measure

**Multiple Search Capabilities**
- **By Name**: Find measures containing a specific word (case-insensitive)
- **By ID**: Search for measures with exact ID match
- **By DataType**: Filter measures by type (FLOAT, INTEGER, BOOLEAN, STRING, DOUBLE, LONG, SHORT)

**Dual Interaction Modes**
- **Command-line Mode**: Direct search with arguments
- **Interactive Mode**: User-friendly prompts with continuous search sessions

**Type-Safe Implementation**
- Enum-based search type management
- Strong typing with Java records and enums
- Lombok annotations for cleaner code

**Comprehensive Testing**
- Unit tests for all core functionality
- Test coverage for edge cases and error scenarios
- JUnit 5 + Lombok integration

**Enterprise-Ready**
- Structured logging with SLF4J
- Proper exception handling
- Clean, maintainable code architecture

## Usage

### Command-Line Mode

Execute direct searches with predefined parameters:

```bash
# Search by measure name (contains "heating")
java -cp "java-recruitment-test/target/classes:java-recruitment-test/target/dependency/*" com.scorpio.Plc name heating

# Search by measure ID (exact match)
java -cp "java-recruitment-test/target/classes:java-recruitment-test/target/dependency/*" com.scorpio.Plc id 1001

# Search by DataType (FLOAT, INTEGER, BOOLEAN, etc.)
java -cp "java-recruitment-test/target/classes:java-recruitment-test/target/dependency/*" com.scorpio.Plc type FLOAT
```

**Or, using Maven:**

```bash
mvn -f java-recruitment-test exec:java \
  -Dexec.mainClass="com.scorpio.Plc" \
  -Dexec.args="name heating"
```

### Interactive Mode

For continuous searches with an intuitive UI:

```bash
# No arguments launches interactive mode
java -cp "java-recruitment-test/target/classes:java-recruitment-test/target/dependency/*" com.scorpio.Plc
```

**Interactive session example:**

```
╔════════════════════════════════════════════════════════════╗
║           SCORPIO - PLC MEASURE SEARCH TOOL                ║
║                                                            ║
║  Supported Search Types:                                   ║
║    • name  - Search by measure name (contains)             ║
║    • id    - Search by measure ID (exact match)            ║
║    • type  - Search by DataType (FLOAT, INTEGER, etc.)     ║
╚════════════════════════════════════════════════════════════╝

Enter search type (name|id|type) or 'quit' to exit: name
Enter search criteria: heating

Measures found by 'name' matching 'heating':
  [1] Root/Building A/Floor 1/Heating Sensor
  [2] Root/Building B/Floor 2/Heating Control
(2 results)
```

## Development & Testing

### Running Tests

```bash
# Run all tests
mvn -f java-recruitment-test test

# Run specific test class
mvn -f java-recruitment-test test -Dtest=CategoryTest

# Generate coverage report
mvn -f java-recruitment-test test jacoco:report
```

### Test Suites

| Test Class | Tests | Coverage |
|---|---|---|
| `CategoryTest` | 13 | Search logic, path building, recursion |
| `MeasureTest` | 20 | Matching logic, case-sensitivity, edge cases |
| `DataTypeTest` | 17 | Enum conversion, validation, edge cases |

## 📝 Data File Format

`bacnetPlcTree.json` structure:

```json
{
  "id": 1,
  "name": "Root",
  "categories": [
    {
      "id": 2,
      "name": "Building A",
      "categories": [...],
      "measures": [
        {
          "id": 1001,
          "name": "Temperature Sensor",
          "dataType": "FLOAT"
        }
      ]
    }
  ],
  "measures": [...]
}
```

## 🔧 Configuration

### Logging (`log4j.properties`)

Customize logging output:
```properties
log4j.rootLogger=INFO, console
log4j.logger.com.scorpio=DEBUG
```

### Maven (`pom.xml`)

Key configurations:
- **Source/Target**: Java 21
- **Dependencies**: Lombok, Jackson, SLF4J, JUnit 5
- **Annotation Processing**: Enabled for Lombok

## 📚 Documentation

Full Javadoc comments are included throughout the codebase:

```bash
# Generate Javadoc
mvn -f java-recruitment-test javadoc:javadoc

# View generated docs
open target/site/apidocs/index.html
```