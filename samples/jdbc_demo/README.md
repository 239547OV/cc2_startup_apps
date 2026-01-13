# Project Context: CC2 Rules Engine Application

## Overview
This is a Spring Boot application that uses the AA CrewComp libraries (cc2-rules-engine, cc2-jobs, cc2-orion-jdbc) to process data from Orion database through a rules engine pipeline.

## Architecture Components

### 1. **cc2-rules-engine** - Rules Engine Framework
The rules engine processes entities through custom business rules.

**Key Classes:**
- `CustomRuleAbstract<T, P>` - Base class for all rules
- `CustomEngineAbstract<T, P>` - Base class for the rules engine
- `Facts` - Context object from jeasy-rules framework

**Current Implementation:**
- `MyRule` - Example rule implementation
- `MainEngine` - Engine that orchestrates all rules
- `MyEntity` - The data entity processed by rules

### 2. **cc2-jobs** - Job Processing Framework
Handles batch job execution with pagination and database interaction.

**Key Classes:**
- `Cc2Job` - Base job class containing job metadata
- `Cc2JobParams` - Job parameters including SQL parameters
- `OrionSqlJobExecutorAbstract<J, R>` - Base executor for Orion SQL jobs
- `Cc2JobServiceInterface<J>` - Interface for job persistence

**Current Implementation:**
- `DemoCc2Job` - Custom job extending Cc2Job
- `CustomOrionSqlJobExecutor` - Executor that queries Orion, processes records, and runs rules engine
- `DemoCc2JobServiceImpl` - Service for job persistence to MongoDB

### 3. **cc2-orion-jdbc** - Orion Database Access
Provides JDBC connectivity to Orion database with pagination support.

**Key Classes:**
- `OrionRecordsExecutor` - Service for executing Orion SQL queries
- `GroupId` - For group-based queries

**Current Implementation:**
- `OrionRecordsExecutorImpl` - Wrapper implementing OrionRecordsExecutorInterface
- `RawOrionRecord` - Entity mapping Orion query results

## Application Flow

```
MainService (on startup)
    ↓
CustomOrionSqlJobExecutor.init() - Initialize with SQL query and parameters
    ↓
CustomOrionSqlJobExecutor.execute() - Execute paginated queries
    ↓
CustomOrionSqlJobExecutor.processPageRecords() - For each page:
    1. Fetch records from Orion (via OrionRecordsExecutorImpl)
    2. Transform RawOrionRecord → MyEntity
    3. Pass to MainEngine.startEngineWithInputData()
    ↓
MainEngine - Applies all registered rules to entities
    ↓
MyRule (and other rules) - Process each entity with business logic
```

---

## How to Create a New Rule

### Step 1: Create the Rule Class

Create a new Java class in the `com.example.demo` package that extends `CustomRuleAbstract<MyEntity, Object>`:

```java
package com.example.demo;

import org.jeasy.rules.api.Facts;
import com.aa.crewcomp.rulesengine.core.CustomRuleAbstract;

public class MyNewRule extends CustomRuleAbstract<MyEntity, Object> {

    public MyNewRule(String name, String description, int priority) {
        super(name, description, priority);
    }

    @Override
    public boolean condition(Facts facts) {
        // Define when this rule should be executed
        MyEntity entity = facts.get("currentRecord");
        // Example: return entity.getBudget() > 1000;
        return true;
    }

    @Override
    public void action(MyEntity currentRecord, Object parentRecord) {
        // Implement the business logic when condition is true
        // Example: currentRecord.setExpenses(currentRecord.getBudget() * 2);
    }

    @Override
    public void handleConditionException(MyEntity currentRow, Exception e) {
        // Handle any exceptions that occur during condition evaluation
        // Log error, set default values, or rethrow as needed
    }

    @Override
    public void handleActionException(MyEntity currentRow, Exception e) {
        // Handle any exceptions that occur during action execution
        // Log error, rollback changes, or rethrow as needed
    }
}
```

### Step 2: Register the Rule in MainEngine

Open `MainEngine.java` and add your new rule to the `addCustomRules()` method:

```java
@Component
public class MainEngine extends CustomEngineAbstract<MyEntity, Object> {

    @Override
    public void addCustomRules() {
        // Existing rules
        this.addCustomRule(new MyRule("MyRule", "Description of MyRule", 1));
        
        // Add your new rule with appropriate priority
        this.addCustomRule(new MyNewRule("MyNewRule", "Description of MyNewRule", 2));
    }
}
```

**Priority Notes:**
- Lower numbers = higher priority (executed first)
- Rules are executed in priority order
- Use priority to control rule execution sequence

---

## Rule Implementation Guidelines

### Method Responsibilities

1. **`condition(Facts facts)`**
   - Returns `true` if the rule should execute, `false` to skip
   - Access the current entity: `facts.get("currentRecord")`
   - Should be side-effect free (don't modify data here)

2. **`action(MyEntity currentRecord, Object parentRecord)`**
   - Contains the main business logic
   - Modify `currentRecord` as needed
   - `parentRecord` can be used for hierarchical data (currently unused - set to Object)

3. **`handleConditionException(MyEntity currentRow, Exception e)`**
   - Called if condition() throws an exception
   - Log and handle gracefully to prevent rule chain breakage

4. **`handleActionException(MyEntity currentRow, Exception e)`**
   - Called if action() throws an exception
   - Log and handle gracefully to prevent rule chain breakage

### Best Practices

- **Single Responsibility**: Each rule should handle one specific business logic
- **Clear Naming**: Use descriptive names that explain what the rule does
- **Error Handling**: Always implement exception handlers with proper logging
- **Immutable Conditions**: Don't modify data in `condition()` method
- **Testing**: Create unit tests for each rule's condition and action
- **Documentation**: Add JavaDoc to explain rule purpose and behavior

---

## Data Flow Details

### Entity Types

- **`RawOrionRecord`**: Raw data from Orion database (column names like RAW_NAME, RAW_BUDGET)
- **`MyEntity`**: Transformed entity used by rules engine (properties like name, budget, expenses)

### Transformation Point

Transform `RawOrionRecord` to `MyEntity` in `CustomOrionSqlJobExecutor.processPageRecords()`:

```java
// Currently: List<MyEntity> input = null; // this.transformAllOrionRecords();
// Implement transformation logic as needed
```

---

## Configuration Files

- **`application.yml`**: MongoDB connections, job parameters
- **`MongoInputDBConfig.java`**: Input MongoDB configuration
- **`MongoOutputDBConfig.java`**: Output MongoDB configuration
- **`AppConfig.java`**: General application configuration

---

## Testing

When creating a new rule, test:
1. Condition evaluation with various entity states
2. Action execution and entity modifications
3. Exception handling for both condition and action
4. Rule priority and execution order in the engine

---

## Common Patterns

### Conditional Rules
```java
@Override
public boolean condition(Facts facts) {
    MyEntity entity = facts.get("currentRecord");
    return entity.getBudget() != null && entity.getBudget() > 0;
}
```

### Validation Rules
```java
@Override
public void action(MyEntity currentRecord, Object parentRecord) {
    if (currentRecord.getExpenses() > currentRecord.getBudget()) {
        // Handle over-budget scenario
    }
}
```

### Calculation Rules
```java
@Override
public void action(MyEntity currentRecord, Object parentRecord) {
    Integer remaining = currentRecord.getBudget() - currentRecord.getExpenses();
    currentRecord.setRemaining(remaining);
}
```

---

## Dependencies Reference

```xml
<!-- Rules Engine -->
<dependency>
    <groupId>com.aa.crewcomp</groupId>
    <artifactId>cc2-rules-engine</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- Job Framework -->
<dependency>
    <groupId>com.aa.crewcomp</groupId>
    <artifactId>cc2-jobs</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- Orion JDBC -->
<dependency>
    <groupId>com.aa.crewcomp</groupId>
    <artifactId>cc2-orion-jdbc</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```
