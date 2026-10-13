# Group19 DevOps Coursework

This repository is developed and maintained by **Group 19 of Edinburgh Napier University** as part of the **DevOps coursework project**.
It serves as a collaborative platform where all team members contribute to the **design, implementation, and delivery** of the project.

---

## Status Badges

![Master Workflow](https://img.shields.io/github/actions/workflow/status/Drewid101/Group19-DevOps/main.yml?branch=master&label=Master%20Build&style=flat-square)
[![LICENSE](https://img.shields.io/github/license/Drewid101/Group19-DevOps.svg?style=flat-square&label=License)](https://github.com/Drewid101/Group19-DevOps/blob/master/LICENSE)
![Develop Workflow](https://img.shields.io/github/actions/workflow/status/Drewid101/Group19-DevOps/main.yml?branch=develop&style=flat-square&label=Develop%20Build)
[![Releases](https://img.shields.io/github/release/Drewid101/Group19-DevOps/all.svg?style=flat-square&label=Release)](https://github.com/Drewid101/Group19-DevOps/releases)
[![codecov](https://codecov.io/gh/Drewid101/Group19-DevOps/graph/badge.svg)](https://codecov.io/gh/Drewid101/Group19-DevOps)

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

Before running the project, make sure the following are installed and configured:

**JDK 24** – Ensure JDK 24 is installed and set as default
**Maven 3.9+** – For build automation and dependency management
**MySQL Database** – Import the `world.sql` dataset before running the application
**Git / GitHub** – For version control and collaboration

---

## Build and Run
To build and run the project locally, follow these steps:

1. **Clean the project**:

    ```bash
    mvn clean
    ```

2. **Validate the project**:

   ```bash
   mvn validate
   ```

3. **Compile the project**:

   ```bash
   mvn compile
   ```

4. **Package the project**:

   ```bash
   mvn package
   ```

5. **Run the project using Docker Compose**:
   Ensure you have a `docker-compose.yml` file in your project directory, then run:

   ```bash
   docker-compose up --build
   ```

This will build the Docker image and start the application as defined in the `docker-compose.yml` file.

6. **After packaging, run the application with:**:

    ```bash
    java -cp target/gp19CourseWork.jar com.napier.gp19.App
    ```
   After run local App.java will connect the database of Docker-Compose and  Markdown Report Files will be generated in the project root directory.
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

At this stage, the repository is intended to be maintained by the project owner **Jude Osamor ** and **Drewid101** only until more collaborators are invited through GitHub repository settings.

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
