package com.napier.gp19;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for generating reports from the World database.
 * This class handles:
 *  - Connecting to and disconnecting from the MySQL database.
 *  - Retrieving and printing various country, city, capital city, population, and language reports.
 *  - Writing the reports to Markdown files using ReportWriter.
 */
public class App {

    /* Connect object used to connect to the MySQL database.
     * Initialized as null and set once a successful connection is made.
     */
    private Connection conn = null;

    /**
     * Gets the current database connection.
     * @return the database connection object
     */
    public Connection getConnection() {
        return this.conn;
    }

    /**
     * Connects to the MySQL database at the specified location.
     * Retries multiple times if connection fails.
     *
     * @param location the database host and port (e.g., localhost:3306)
     * @param delay delay in milliseconds between connection retries
     */
    public void connect(String location, int delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("Connection retry delay cannot be negative.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load the MySQL driver.", e);
        }

        String database = environmentValue("DB_NAME", "world");
        String user = environmentValue("DB_USER", "root");
        String password = environmentValue("DB_PASSWORD", "root");
        String url = "jdbc:mysql://" + location + "/" + database
                + "?useSSL=false&allowPublicKeyRetrieval=true";
        int retries = environmentInteger("DB_CONNECT_RETRIES", 30, 1);
        SQLException lastException = null;

        for (int attempt = 1; attempt <= retries; ++attempt) {
            try {
                if (attempt > 1 && delay > 0) {
                    Thread.sleep(delay);
                }

                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Successfully connected");
                return;
            } catch (SQLException sqle) {
                lastException = sqle;
                System.out.println("Failed to connect to database attempt " + attempt);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Database connection retry was interrupted.", ie);
            }
        }

        throw new IllegalStateException(
                "Could not connect to the database after " + retries + " attempts.",
                lastException);
    }

    /**
     * Disconnects from the MySQL database.
     * Closes the connection if it is open.
     */
    public void disconnect() {
        if (conn != null) {
            try {
                // Close connection
                conn.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    private static String environmentValue(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private static int environmentInteger(String name, int defaultValue, int minimum) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return parseInteger(name, value, minimum);
    }

    private static int parseInteger(String name, String value, int minimum) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < minimum) {
                throw new IllegalArgumentException(name + " must be at least " + minimum + ".");
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(name + " must be a whole number.", e);
        }
    }

    /**
     * Main method: executes all reports and writes them to Markdown files.
     *
     * @param args optional command-line arguments: database location and delay
     */
    public static void main(String[] args) {
        App app = new App();
        ReportWriter writer = new ReportWriter();

        String host = environmentValue("DB_HOST", "localhost");
        int port = environmentInteger("DB_PORT", 33060, 1);
        String location = args.length >= 1 && !args[0].isBlank()
                ? args[0]
                : host + ":" + port;
        int delay = args.length >= 2
                ? parseInteger("Connection retry delay", args[1], 0)
                : environmentInteger("DB_CONNECT_RETRY_DELAY_MS", 1000, 0);

        app.connect(location, delay);

        // ============================================================
        // Reports 1-32: Generate, print, and save all reports
        // ============================================================

        // Example report workflow:
        // 1. Create report object (e.g., CountriesWorldReport)
        // 2. Retrieve data from database
        // 3. Print report to console
        // 4. Write report to Markdown file using ReportWriter

        // Reports include:
        // - Countries (world, continent, region, top N)
        // - Cities (world, continent, region, country, district, top N)
        // - Capital cities (world, continent, region, top N)
        // - Population (world, continent, region, country, district, city)
        // - Language population

        //============================================================
        // REPORT 1. Country world report
        //============================================================
        CountriesWorldReport countriesWorld = new CountriesWorldReport();
        ArrayList<Country> countriesWorldList = countriesWorld.getCountriesWorldReport(app.conn);
        countriesWorld.printCountriesWorldReport(countriesWorldList);
        writer.outputCountries(countriesWorldList, "01_Countries_World_Report.md", "Report 1: Countries in the World");

        //============================================================
        // REPORT 2. Country Continent report
        //============================================================
        String countriesContinent = "Asia";
        CountriesContinentReport countriesContinentReport = new CountriesContinentReport();
        ArrayList<Country> countriesContinentList = countriesContinentReport.getCountriesContinentReport(app.conn, countriesContinent);
        countriesContinentReport.printCountriesContinentReport(countriesContinent, countriesContinentList);
        writer.outputCountries(countriesContinentList, "02_Countries_Continent_Report.md", "Report 2: Countries in " + countriesContinent);

        //============================================================
        // REPORT 3. Country Region report
        //============================================================
        String countriesRegion = "Middle East";
        CountriesRegionReport countriesRegionReport = new CountriesRegionReport();
        ArrayList<Country> countriesRegionList = countriesRegionReport.getCountriesRegionReport(app.conn, countriesRegion);
        countriesRegionReport.printCountriesRegionReport(countriesRegion, countriesRegionList);
        writer.outputCountries(countriesRegionList, "03_Countries_Region_Report.md", "Report 3: Countries in " + countriesRegion);

        //============================================================
        // REPORT 4: Top N Countries in the World
        //============================================================
        ArrayList<Country> topNCountries = countriesWorld.getTopNCountriesWorldReport(app.conn,10);
        countriesWorld.printTopNCountriesWorldReport(topNCountries,10);
        writer.outputCountries(topNCountries, "04_TopN_Countries_World_Report.md", "Report 4: Top 10 Countries in the World");

        //============================================================
        // REPORT 5: Top N Countries in the Continent
        //============================================================
        ArrayList<Country> topNCountriesContinent = countriesContinentReport.getTopNCountriesContinentReport(app.conn,countriesContinent,10);
        countriesContinentReport.printTopNCountriesContinentReport(countriesContinent,topNCountriesContinent,10);
        writer.outputCountries(topNCountriesContinent, "05_TopN_Countries_Continent_Report.md", "Report 5: Top 10 Countries in " + countriesContinent);

        //============================================================
        // REPORT 6: Top N Countries in the Region
        //============================================================
        ArrayList<Country> topNCountriesRegion = countriesRegionReport.getTopNCountriesRegionReport(app.conn,countriesRegion,10);
        countriesRegionReport.printTopNCountriesRegionReport(countriesRegion,topNCountriesRegion,10);
        writer.outputCountries(topNCountriesRegion, "06_TopN_Countries_Region_Report.md", "Report 6: Top 10 Countries in " + countriesRegion);

        // -------------------------------------------------------------------------

        //============================================================
        // REPORT 7: All Cities in the world
        //============================================================
        CitiesWorldReport citiesWorldReport = new CitiesWorldReport();
        ArrayList<City> citiesWorldList = citiesWorldReport.getCitiesWorldReport(app.conn);
        citiesWorldReport.printCitiesWorldReport(citiesWorldList);
        writer.outputCities(citiesWorldList, "07_Cities_World_Report.md", "Report 7: Cities in the World");

        //============================================================
        // REPORT 8: All Cities in the continent
        //============================================================
        String citiesContinent = "Asia";
        CitiesContinentReport citiesContinentReport = new CitiesContinentReport();
        ArrayList<City> citiesContinentList = citiesContinentReport.getCitiesContinentReport(app.conn, citiesContinent);
        citiesContinentReport.printCitiesContinentReport(citiesContinentList, citiesContinent);
        writer.outputCities(citiesContinentList, "08_Cities_Continent_Report.md", "Report 8: Cities in " + citiesContinent);

        //============================================================
        // REPORT 9: All Cities in the region
        //============================================================
        String citiesRegion = "Eastern Asia";
        CitiesRegionReport citiesRegionReport = new CitiesRegionReport();
        ArrayList<City> citiesRegionList = citiesRegionReport.getCitiesRegionReport(app.conn, citiesRegion);
        citiesRegionReport.printCitiesRegionReport(citiesRegionList, citiesRegion);
        writer.outputCities(citiesRegionList, "09_Cities_Region_Report.md", "Report 9: Cities in " + citiesRegion);

        //============================================================
        // REPORT 10: All Cities in the country
        //============================================================
        String citiesCountry = "Myanmar";
        CitiesCountryReport citiesCountryReport = new CitiesCountryReport();
        ArrayList<City> cityCountryList = citiesCountryReport.getCitiesCountryReport(app.conn, citiesCountry);
        citiesCountryReport.printCitiesCountryReport(cityCountryList, citiesCountry);
        writer.outputCities(cityCountryList, "10_Cities_Country_Report.md", "Report 10: Cities in " + citiesCountry);


        //============================================================
        // REPORT 11: All Cities in the district
        //============================================================
        String citiesDistrict = "Rio de Janeiro";
        CitiesDistrictReport citiesDistrictReport = new CitiesDistrictReport();
        ArrayList<City> cityDistrictList = citiesDistrictReport.getCitiesDistrictReport(app.conn, citiesDistrict);
        citiesDistrictReport.printCitiesDistrictReport(cityDistrictList, citiesDistrict);
        writer.outputCities(cityDistrictList, "11_Cities_District_Report.md", "Report 11: Cities in " + citiesDistrict);

        //============================================================
        // REPORT 12: Top N Cities in the world
        //============================================================
        int citiesWorldN = 10;
        ArrayList<City> topNCitiesWorldList = citiesWorldReport.getTopNCitiesWorldReport(app.conn, citiesWorldN);
        citiesWorldReport.printTopNCitiesWorldReport(topNCitiesWorldList, citiesWorldN);
        writer.outputCities(topNCitiesWorldList, "12_TopN_Cities_World_Report.md", "Report 12: Top 10 Cities in the World");

        //============================================================
        // REPORT 13: Top N Cities in the continent
        //============================================================
        int citiesContinentN = 10;
        ArrayList<City> topNCitiesContinentList = citiesContinentReport.getTopNCitiesContinentReport(app.conn, citiesContinent, citiesContinentN);
        citiesContinentReport.printTopNCitiesContinentReport(topNCitiesContinentList, citiesContinent, citiesContinentN);
        writer.outputCities(topNCitiesContinentList, "13_TopN_Cities_Continent_Report.md", "Report 13: Top 10 Cities in " + citiesContinent);

        //============================================================
        // REPORT 14: Top N Cities in the region
        //============================================================
        int citiesRegionN = 10;
        ArrayList<City> topNCitiesRegionList = citiesRegionReport.getTopNCitiesRegionReport(app.conn, citiesRegion, citiesRegionN);
        citiesRegionReport.printTopNCitiesRegionReport(topNCitiesRegionList, citiesRegion, citiesRegionN);
        writer.outputCities(topNCitiesRegionList, "14_TopN_Cities_Region_Report.md", "Report 14: Top 10 Cities in " + citiesRegion);

        //============================================================
        // REPORT 15: Top N Cities in the country
        //============================================================
        int citiesCountryN = 10;  // Number of top cities to display
        ArrayList<City> topNCitiesCountryList = citiesCountryReport.getTopNCitiesCountryReport(app.conn, citiesCountry, citiesCountryN);
        citiesCountryReport.printTopNCitiesCountryReport(topNCitiesCountryList, citiesCountry, citiesCountryN);
        writer.outputCities(topNCitiesCountryList, "15_TopN_Cities_Country_Report.md", "Report 15: Top 10 Cities in " + citiesCountry);

        //============================================================
        // REPORT 16: Top N Cities in the district
        //============================================================
        int citiesDistrictN = 10;  // Number of top cities to display
        ArrayList<City> topNCitiesDistrictList = citiesDistrictReport.getTopNCitiesDistrictReport(app.conn, citiesDistrict, citiesDistrictN);
        citiesDistrictReport.printTopNCitiesDistrictReport(topNCitiesDistrictList, citiesDistrict, citiesDistrictN);
        writer.outputCities(topNCitiesDistrictList, "16_TopN_Cities_District_Report.md", "Report 16: Top 10 Cities in " + citiesDistrict);

        //============================================================
        // REPORT 17: Capital Cities in the World
        //============================================================
        // Create instance of CapitalCities_World class
        CapitalCitiesWorldReport capitalCitiesWorldReport = new CapitalCitiesWorldReport();

        // Get all capital cities in the world ordered by population (descending)
        ArrayList<City> capitalsWorldList = capitalCitiesWorldReport.getAllCapitalCitiesInWorldByPopulation(app.conn);

        // Print the capital cities in world report
        capitalCitiesWorldReport.printAllCapitalCitiesInWorldByPopulation(capitalsWorldList);
        writer.outputCapitalCities(capitalsWorldList, "17_CapitalCities_World_Report.md", "Report 17: Capital Cities in the World");

        //============================================================
        // REPORT 18: Capital Cities in a Continent
        //============================================================
        // Define the continent for which the report will be generated.
        String capitalCitiesContinent = "Asia";

        // Create instance to generate the continent level report
        CapitalCitiesContinentReport capitalCitiesContinentReport = new CapitalCitiesContinentReport();

        // Get all capital cities within the specified continent, ordered by population (Descending)
        ArrayList<City> capitalsContinentList = capitalCitiesContinentReport.getAllCapitalCitiesInContinentByPopulation(app.conn, capitalCitiesContinent);

        // Print the capital cities in the specified continent report.
        capitalCitiesContinentReport.printAllCapitalCitiesInContinentByPopulation(capitalsContinentList,capitalCitiesContinent);
        writer.outputCapitalCities(capitalsContinentList, "18_CapitalCities_Continent_Report.md", "Report 18: Capital Cities in " + capitalCitiesContinent);


        //============================================================
        // REPORT 19: Capital Cities in a Region
        //============================================================
        // Define the region for which the report will be generated.
        String capitalCitiesRegion = "Middle East";

        // Create instance to generate the region-level report.
        CapitalCitiesRegionReport capitalCitiesRegionReport = new CapitalCitiesRegionReport();

        // Retrieve all capital cities with the specified region, ordered by population (Descending)
        ArrayList<City> capitalsRegionList = capitalCitiesRegionReport.getAllCapitalCitiesInRegionByPopulation(app.conn, capitalCitiesRegion);

        // Print the capital cities in the specified region report
        capitalCitiesRegionReport.printAllCapitalCitiesInRegionByPopulation(capitalsRegionList, capitalCitiesRegion);
        writer.outputCapitalCities(capitalsRegionList, "19_CapitalCities_Region_Report.md", "Report 19: Capital Cities in " + capitalCitiesRegion);

        //============================================================
        // REPORT 20: Top N Capital Cities in the World
        //============================================================
        //Get top n capital cities in the world, ordered by population (Descending)
        ArrayList<City> topNCapitalsWorldList = capitalCitiesWorldReport.getTopNCapitalCitiesInWorldByPopulation(app.conn, 10);

        //print top n capital cities in the world
        capitalCitiesWorldReport.printTopNCapitalCitiesInWorldByPopulation(topNCapitalsWorldList,10);
        writer.outputCapitalCities(topNCapitalsWorldList, "20_TopN_CapitalCities_World_Report.md", "Report 20: Top 10 Capital Cities in the World");

        //============================================================
        // REPORT 21: Top N Capital Cities in a Continent
        //============================================================
        //Get top n capital cities within the specified continent, ordered by population (Descending)
        ArrayList<City> topNCapitalsContinentList = capitalCitiesContinentReport.getTopNCapitalCitiesInContinentByPopulation(app.conn,capitalCitiesContinent,10);

        //Print top n capital cities in the specified continent report.
        capitalCitiesContinentReport.printTopNCapitalCitiesInContinentByPopulation(topNCapitalsContinentList,capitalCitiesContinent,10);
        writer.outputCapitalCities(topNCapitalsContinentList, "21_TopN_CapitalCities_Continent_Report.md", "Report 21: Top 10 Capital Cities in " + capitalCitiesContinent);

        //============================================================
        // REPORT 22: Top N Capital Cities in a Region
        //============================================================
        // Retrieve all capital cities with the specified region, ordered by population (Descending)
        ArrayList<City> topNCapitalsRegionList = capitalCitiesRegionReport.getTopNCapitalCitiesInRegionByPopulation(app.conn, capitalCitiesRegion,10);

        // Print the capital cities in the specified region report
        capitalCitiesRegionReport.printTopNCapitalCitiesInRegionByPopulation(topNCapitalsRegionList, capitalCitiesRegion,10);
        writer.outputCapitalCities(topNCapitalsRegionList, "22_TopN_CapitalCities_Region_Report.md", "Report 22: Top 10 Capital Cities in " + capitalCitiesRegion);

        //------------------------------------------

        //================================================================
        // REPORT 23: Population of a continent (live in/not live in city)
        //================================================================
        PopulationContinentReport continentReport = new PopulationContinentReport();
        List<Country> populations = continentReport.getPopulationCityVsNonCityByContinent(app.conn);
        // Print the report
        continentReport.printPopulationCityVsNonCityByContinent(populations);
        writer.outputContinentPopulation(populations, "23_Population_Continent_Report.md", "Report 23: Population by Continent");

        //================================================================
        // REPORT 24: Population of a region (live in/not live in city)
        //================================================================
        PopulationRegionReport regionReport = new PopulationRegionReport();
        List<Country> regions = regionReport.getPopulationRegionDetailsReport(app.conn);
        // Print the report
        regionReport.printPopulationRegionDetailsReport(regions);
        writer.outputPopulationByGroup(regions, "24_Population_Region_Report.md", "Report 24: Population by Region", "Region");

        //================================================================
        // REPORT 25: Population of a country (live in/not live in city)
        //================================================================
        PopulationCountryReport countryReport = new PopulationCountryReport();
        List<Country> countryPopulations = countryReport.getPopulationCityVsNonCityByCountry(app.conn);
        // Print the report
        countryReport.printPopulationCityVsNonCityByCountry(countryPopulations);
        writer.outputPopulationByGroup(countryPopulations, "25_Population_Country_Report.md", "Report 25: Population by Country", "Country");

        //========================================
        // REPORT 26: Population of the world
        //========================================
        PopulationWorldReport worldReport = new PopulationWorldReport();
        PopulationWorldReport.PopulationData worldPop = worldReport.getPopulationWorldReport(app.conn);
        // Print the report
        worldReport.printPopulationWorldReport(worldPop);
        writer.outputPopulationWorld(worldPop, "26_Population_World_Report.md");

        //========================================
        // REPORT 27: Population of the continent
        //========================================
        String continentVariable = "Asia"; // Replace with any continent you want
        PopulationContinentReport populationContinentReport = new PopulationContinentReport();
        List<Country> populationContinentList = populationContinentReport.getPopulationContinentReport(app.conn);
        // Filter the list to only include the chosen continent
        List<Country> filteredList = populationContinentList.stream()
                .filter(c -> c.getContinent().equalsIgnoreCase(continentVariable))
                .toList(); // Java 16+, or use Collectors.toList() for older versions
        // Print the report for that continent
        populationContinentReport.printPopulationContinentReport(filteredList);
        // Write the filtered continent report (use outputContinentPopulation so "Continent" column shows correctly)
        writer.outputContinentPopulation(filteredList, "27_Population_Specific_Continent_Report.md", "Report 27: Population of Continent (" + continentVariable + ")");

        //========================================
        // REPORT 28: Population of the region
        //========================================
        String regionVariable = "Southern Europe"; // Replace with any region you want
        PopulationRegionReport populationRegionReport = new PopulationRegionReport();
        List<Country> populationRegionList = populationRegionReport.getPopulationRegionReport(app.conn);
        // Print the report for the selected region only
        populationRegionReport.printPopulationRegionReport(populationRegionList, regionVariable);
        List<Country> filteredRegionList = populationRegionList.stream()
                .filter(c -> c.getRegion().equalsIgnoreCase(regionVariable))
                .toList(); // For Java 16+, otherwise use Collectors.toList()
        writer.outputPopulationByGroup(filteredRegionList, "28_Population_Specific_Region_Report.md", "Report 28: Population of Region (" + regionVariable + ")", "Region");


        //========================================
        // REPORT 29: Population of the country
        //========================================
        String countryVariable = "Japan"; // Replace with the country you want
        PopulationCountryReport populationCountryReport = new PopulationCountryReport();
        List<Country> populationCountryList = populationCountryReport.getPopulationCountryReport(app.conn);
        // Print the report for the selected country only
        populationCountryReport.printPopulationCountryReport(populationCountryList, countryVariable);
        List<Country> filteredCountryList = populationCountryList.stream()
                .filter(c -> c.getCountryName().equalsIgnoreCase(countryVariable))
                .toList();
        writer.outputPopulationByGroup(filteredCountryList, "29_Population_Specific_Country_Report.md", "Report 29: Population of Country (" + countryVariable + ")", "Country");

        //========================================
        // REPORT 30: Population of the district
        //========================================
        String districtVariable = "California"; // Replace with your desired district
        PopulationDistrictReport populationDistrictReport = new PopulationDistrictReport();
        List<Country> populationDistrictList = populationDistrictReport.getPopulationDistrictReport(app.conn);
        // Print the report for the selected district including percentages
        populationDistrictReport.printPopulationDistrictReport(populationDistrictList, districtVariable);
        List<Country> filteredDistrictList = populationDistrictList.stream()
                .filter(c -> c.getCountryName().equalsIgnoreCase(districtVariable))
                .toList();
        writer.outputPopulationByGroup(filteredDistrictList,
                "30_Population_District_Report.md",
                "Report 30: Population of District (" + districtVariable + ")",
                "District");

        //========================================
        // REPORT 31: Population of the city
        //========================================
        String cityVariable = "Tokyo";
        PopulationCityReport cityReport = new PopulationCityReport();
        List<City> cityList = cityReport.getPopulationCityReport(app.conn);
        // Print a specific city
        cityReport.printPopulationCityReport(cityList,cityVariable);
        List<City> filteredCityList = cityList.stream()
                .filter(c -> c.getCityName().equalsIgnoreCase(cityVariable))
                .toList();
        writer.outputPopulationCityReport(filteredCityList, "31_Population_City_Report.md");

        //============================================================
        // REPORT 32: Language Population
        //============================================================
        // Create instance of language_population class
        LanguagePopulationReport report = new LanguagePopulationReport();
        ArrayList<CountryLanguage> languageReport = report.getLanguagePopulationReport(app.conn);
        report.printLanguagePopulationReport(languageReport);
        writer.outputLanguagePopulationReport(languageReport, "32_Language_Population_Report.md");

        //disconnect from database
        app.disconnect();
    }

}