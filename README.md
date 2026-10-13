# Group19 DevOps Coursework

This repository is developed and maintained by **Group 19 of Edinburgh Napier University** as part of the **DevOps coursework project**.
It serves as a collaborative platform where all team members contribute to the **design, implementation, and delivery** of the project.

---

## Status Badges

![Master Workflow](https://img.shields.io/github/actions/workflow/status/osamorju/GROUP19-PREDEVELOPMENT/main.yml?branch=master&label=Master%20Build&style=flat-square)
[![LICENSE](https://img.shields.io/github/license/osamorju/GROUP19-PREDEVELOPMENT.svg?style=flat-square&label=License)](https://github.com/osamorju/GROUP19-PREDEVELOPMENT/blob/master/LICENSE)
[![Releases](https://img.shields.io/github/release/osamorju/GROUP19-PREDEVELOPMENT/all.svg?style=flat-square&label=Release)](https://github.com/osamorju/GROUP19-PREDEVELOPMENT/releases)
[![codecov](https://codecov.io/gh/osamorju/GROUP19-PREDEVELOPMENT/graph/badge.svg)](https://codecov.io/gh/osamorju/GROUP19-PREDEVELOPMENT)

---
## Overview

The project is a **Population Reporting System** that generates reports on global, continental, and regional populations using the `world.sql` dataset.
It demonstrates **DevOps principles** such as CI/CD, automated builds, containerization, and collaborative development.

---

## Purpose

The purpose of this system is to:

* Provide population insights across different geographic levels.
* Showcase integration of databases, automation tools, and DevOps pipelines.
* Enable continuous improvement and teamwork in software delivery.

---

## Requirements

To run the published images, install Docker with Docker Compose.

To develop the Java source directly, also install:

- JDK 24
- Maven 3.9 or later
- Git

MySQL does not need to be installed. The database container loads
`database/db/world.sql` automatically.

---

## Build the Images from Source

Create the local environment file, build both images, and run them:

```bash
docker compose build
docker compose up --no-build --abort-on-container-exit --exit-code-from app
docker compose cp app:/app/reports/. reports/
docker compose down --volumes
```

This school project uses `root` as the MySQL password to keep the coursework
setup simple. A production system should use a limited database user, a strong
unique password, and Docker secrets or a managed secret service.

The application image compiles the Java source with Maven inside its build
stage. The final image contains the Java runtime and the compiled JAR, but it
does not contain Maven or the source files.

## Run the Published Images

The GitHub Actions workflow publishes these images:

- `ghcr.io/osamorju/group19-predevelopment-app:latest`
- `ghcr.io/osamorju/group19-predevelopment-database:latest`

Generate the reports without building the source:

```bash
docker compose pull
docker compose up --no-build --abort-on-container-exit --exit-code-from app
docker compose cp app:/app/reports/. reports/
docker compose down --volumes
```

The pipeline also publishes immutable `sha-<commit>` tags. New GHCR packages
are private by default, so the repository owner must change both package
visibility settings to **Public** after the first successful publication.

## CI/CD Pipeline

The workflow:

1. Runs unit and integration tests while both container images build.
2. Builds application and database images for AMD64 and ARM64.
3. Publishes immutable `sha-<commit>` image tags.
4. Runs those immutable images together.
5. Checks that all 32 reports were generated.
6. Moves the tested images to the `latest` tag.
7. Releases the exact tested JAR and publishes the reports branch.

Documentation-only changes do not start the full container pipeline.
---

## Kanban Board

You can follow our tasks and progress here:
[Group 19 Backlog / Kanban Board](https://github.com/users/osamorju/projects/1)

---

## Documents

You can also view the following key documents:

- [Code of Conduct](CODE_OF_CONDUCT.md)
- [Product Backlog](PRODUCT_BACKLOG.md)
- [Use Cases](USE_CASES.md)
- [Contribution Guidelines](CONTRIBUTING.md)
- [Security Policy](SECURITY.md)
- [Running Locally and Publishing Reports](LOCAL_RUN_AND_REPORTS.md)
---

## Contribution Guidelines

At this stage, the repository is intended to be maintained by the project owner **Jude Osamor** and **Drewid101** only until more collaborators are invited through GitHub repository settings.

We welcome contributions from team members.
Please ensure:

* Code follows best practices and is properly documented.
* Branch naming follows the `feature/`, `bugfix/`, or `hotfix/` convention.
* Pull requests are reviewed before merging.

(Refer to the [Code of Conduct](CODE_OF_CONDUCT.md), [Contribution Guidelines](CONTRIBUTING.md))

---



##  All reports requirements have been implemented, which is 100% of the total requirements.
| ID | Name                                                                                                                                     | Met|
| -: | ---------------------------------------------------------------------------------------------------------------------------------------- |------|
|  1 | All the countries in the world organised by largest population to smallest.                                                              | Met|
|  2 | All the countries in a continent organised by largest population to smallest.                                                            | Met|
|  3 | All the countries in a region organised by largest population to smallest.                                                               | Met|
|  4 | The top N populated countries in the world where N is provided by the user.                                                              | Met|
|  5 | The top N populated countries in a continent where N is provided by the user.                                                            | Met|
|  6 | The top N populated countries in a region where N is provided by the user.                                                               | Met|
|  7 | All the cities in the world organised by largest population to smallest.                                                                 | Met|
|  8 | All the cities in a continent organised by largest population to smallest.                                                               | Met|
|  9 | All the cities in a region organised by largest population to smallest.                                                                  | Met|
| 10 | All the cities in a country organised by largest population to smallest.                                                                 | Met|
| 11 | All the cities in a district organised by largest population to smallest.                                                                | Met|
| 12 | The top N populated cities in the world where N is provided by the user.                                                                 | Met|
| 13 | The top N populated cities in a continent where N is provided by the user.                                                               | Met|
| 14 | The top N populated cities in a region where N is provided by the user.                                                                  | Met|
| 15 | The top N populated cities in a country where N is provided by the user.                                                                 | Met|
| 16 | The top N populated cities in a district where N is provided by the user.                                                                | Met|
| 17 | All the capital cities in the world organised by largest population to smallest.                                                         | Met|
| 18 | All the capital cities in a continent organised by largest population to smallest.                                                       | Met|
| 19 | All the capital cities in a region organised by largest to smallest.                                                                     | Met|
| 20 | The top N populated capital cities in the world where N is provided by the user.                                                         | Met|
| 21 | The top N populated capital cities in a continent where N is provided by the user.                                                       | Met|
| 22 | The top N populated capital cities in a region where N is provided by the user.                                                          | Met|
| 23 | The population of people, people living in cities, and people not living in cities in each continent.                                    | Met|
| 24 | The population of people, people living in cities, and people not living in cities in each region.                                       | Met|
| 25 | The population of people, people living in cities, and people not living in cities in each country.                                      | Met|
| 26 | The population of the world.                                                                                                             | Met|
| 27 | The population of a continent.                                                                                                           | Met|
| 28 | The population of a region.                                                                                                              | Met|
| 29 | The population of a country.                                                                                                             | Met|
| 30 | The population of a district.                                                                                                            | Met|
| 31 | The population of a city.                                                                                                                | Met|
| 32 | Number of people who speak (from greatest to smallest) and percentage of world population for: Chinese, English, Hindi, Spanish, Arabic. | Met|

---

# 📋 PMD Code Style Fix Report

## Rule: LocalVariableNamingConventions
### 🔍 Description

PMD enforces that **local variable names** follow standard Java naming conventions:

* Must begin with a **lowercase letter**
* Should use **camelCase**
* Underscores (`_`) are **discouraged** 
* Should be descriptive and readable

---

### ❌ Before
Underscores used in local variable naming:

```java 
        String countries_Continent = "Asia";
```

### ✅ After
CamelCase used instead:
```java
        String countriesContinent = "Asia";
   ```

### 🛠 Explanation
The variable like `countries_Continent` violates PMD’s `LocalVariableNamingConventions` rule because:


* It contains an underscore
* It mixes lowercase and uppercase in an inconsistent format
* It does not follow Java camelCase style

Renaming it to countriesContinent resolves the PMD violation and aligns with Java standard naming practices.

## Rule: ConstantNamingConventions
### 🔍 Description
PMD requires static final fields (constants) to be named using uppercase letters with underscores (SNAKE_CASE).

#### ❌ Before
```Java
    private static final Logger logger = Logger.getLogger(MyClass.class.getName());
   ```

#### ✅ After
```Java
   private static final Logger LOGGER = Logger.getLogger(MyClass.class.getName());
   ```

### 🛠 Explanation
PMD flagged logger for breaking constant naming rules.
Renaming it to LOGGER ensures it follows the uppercase constant naming convention for static final fields.

## Rule: MethodNamingConventions
### 🔍 Description

PMD enforces that method names must follow standard Java naming conventions:

* Method names must be written in lowerCamelCase 
* Should NOT contain underscores (_)
* Should start with a verb describing the action (e.g., get, set, compute, build)
* Must be descriptive, readable, and consistently formatted

#### ❌ Before
Underscores used in method name:
```Java
   public ArrayList<Country> getCountries_World_Report(Connection conn);
```

#### ✅ After
CamelCase method name following Java convention:
``` Java
    public ArrayList<Country> getCountriesWorldReport(Connection conn);
```

### 🛠 Explanation
The original method name getCountries_World_Report violates PMD’s MethodNamingConventions rule because:

* It contains multiple underscores 
* It is not in Java's standard lowerCamelCase format 
* PMD flags underscores in method names unless they are test methods (e.g., JUnit naming pattern)

Renaming the method to getCountriesWorldReport ensures:
* Compliance with PMD rules
* Proper Java naming convention
* Improved readability and consistency across the codebase
