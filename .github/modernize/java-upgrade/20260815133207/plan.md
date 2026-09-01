# Upgrade Plan: mecanica (20260815133207)

- **Generated**: 2026-08-15 13:32:07
- **HEAD Branch**: N/A
- **HEAD Commit ID**: N/A

## Available Tools

**JDKs**
- JDK 26.0.1: C:\Program Files\Semeru\jdk-26.0.1.8-openj9\bin (available in environment; newer than target LTS)
- JDK 25: **<TO_BE_INSTALLED>** (required for the target LTS runtime)
- JDK 21: not available (baseline will be skipped)

**Build Tools**
- Maven Wrapper: 3.9.16 (defined in .mvn/wrapper/maven-wrapper.properties; compatible with Java 25)

## Guidelines

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

- Upgrade to the latest LTS Java runtime: Java 25.
- Prefer the project’s Maven Wrapper for build verification.
- Preserve the existing Spring Boot 4.1.0 runtime and application behavior.

## Options

- Working branch: appmod/java-upgrade-20260815133207
- Run tests before and after the upgrade: true

## Upgrade Goals

- Java 25

## Technology Stack

| Technology/Dependency | Current | Min Compatible Version | Why Incompatible |
| ---------------------- | ------- | ---------------------- | --------------- |
| Java | 21 | 25 | User requested latest LTS runtime |
| Spring Boot | 4.1.0 | 4.1.0 | Already compatible with Java 21+; no framework jump required |
| Maven Wrapper | 3.9.16 | 3.9.16 | Compatible with Java 25 |
| Java compiler target | 21 | 25 | Must be updated to target the user-requested LTS release |

## Derived Upgrades

- Java 21 -> 25 runtime target requires updating the project Java version property in pom.xml.
- Maven Wrapper remains compatible; no toolchain upgrade is required because 3.9.16 is already current enough for Java 25.
- Spring Boot 4.1.0 already targets modern Java runtimes, reducing the risk of framework-level migration work.

## Impact Analysis

### Subsection: Dependency Changes

| File | Dependency | Current | Action | Target | Reason |
|------|-----------|---------|--------|--------|--------|
| pom.xml | java.version | 21 | upgrade | 25 | User-requested Java LTS target |

### Subsection: Source Code Changes

| File | Location | Current | Required Change | Reason |
|------|----------|---------|----------------|--------|
| No source changes expected | project compiles under Java 21+ Spring Boot 4.1.0 | N/A | Verify and adjust only if compiler/test failures appear | Current framework stack already matches Java 25 compatibility expectations |

### Subsection: Configuration Changes

| File | Property/Setting | Current | Required Change | Reason |
|------|------------------|---------|-----------------|--------|
| pom.xml | java.version | 21 | 25 | Required runtime target update |

### Subsection: CI/CD Changes

| File | Location | Current | Required Change |
|------|----------|---------|----------------|
| None found | project uses wrapper and local Java toolchain | N/A | No CI config changes identified in the repository |

### Subsection: Risks & Warnings

- **Java 25 compliance check**: The project is already on Spring Boot 4.1.0 and should be compatible with recent Java LTS releases, but this must be validated with the actual compiler and test run. **Mitigation**: run Maven compile and full tests under Java 25 and fix any compatibility issues surfaced by the toolchain.
- **JDK 25 availability**: The environment has Java 26 installed, but the target is Java 25; installation is required to validate the exact LTS runtime. **Mitigation**: install JDK 25 and set the Maven toolchain to it for build verification.

## Upgrade Steps

- Step 1: Install Java 25 runtime
  - **Rationale**: The project must be built and validated against the requested LTS runtime before finalizing the upgrade.
  - **Changes to Make**: Install JDK 25 and confirm the path is available to the build.
  - **Verification**: `java -version` and `javac -version` under the JDK 25 installation path.

- Step 2: Update project Java target to 25
  - **Rationale**: This is the minimal necessary change to align the project with the requested LTS runtime.
  - **Changes to Make**: Apply the `java.version` property change in pom.xml and keep the project on the existing Spring Boot 4.1.0 stack.
  - **Verification**: `./mvnw -q -DskipTests compile` using JDK 25.

- Step 3: Compile and repair compatibility issues
  - **Rationale**: The current stack is already close to the target, but compile issues can still emerge once Java 25 is used.
  - **Changes to Make**: Fix any compiler warnings/errors or JDK compatibility issues discovered in main and test source.
  - **Verification**: `./mvnw -q test-compile` using JDK 25.

- Step 4: Validate with full test suite
  - **Rationale**: The application must pass all tests under the target runtime to satisfy the upgrade success criteria.
  - **Changes to Make**: Fix any failing tests or runtime compatibility issues until the suite is green.
  - **Verification**: `./mvnw -q test` using JDK 25.

- Step 5: Final validation and summary
  - **Rationale**: Confirm the upgrade is complete and provide evidence of the Java 25 runtime adoption.
  - **Changes to Make**: Re-run final verification and prepare the summary record.
  - **Verification**: `./mvnw -q clean verify` using JDK 25.
