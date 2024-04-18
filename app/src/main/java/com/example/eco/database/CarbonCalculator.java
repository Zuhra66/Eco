package com.example.eco.database;

public class CarbonCalculator {
    private double totalEmissions;

    public CarbonCalculator() {
        this.totalEmissions = 0.0;
    }

    // Method to calculate emissions for transportation
    public void calculateTransportationEmissions(String choice) {
        switch (choice) {
            case "car":
                totalEmissions += 2.0; // Example emissions for car travel in kg CO2 per mile
                break;
            case "public_transport":
                totalEmissions += 0.5; // Example emissions for public transportation in kg CO2 per mile
                break;
            case "air_travel":
                totalEmissions += 1.0; // Example emissions for air travel in kg CO2 per mile
                break;

        }
    }

    // Method to calculate emissions for energy consumption
    public void calculateEnergyEmissions(String choice) {
        switch (choice) {
            case "heating":
                totalEmissions += 5.0; // Example emissions for heating in kg CO2 per hour
                break;
            case "cooling":
                totalEmissions += 3.0; // Example emissions for cooling in kg CO2 per hour
                break;
            case "electricity":
                totalEmissions += 0.5; // Example emissions for electricity usage in kg CO2 per kWh
                break;

        }
    }

    // Method to calculate emissions for dietary choices
    public void calculateDietaryEmissions(String choice) {
        switch (choice) {
            case "meat":
                totalEmissions += 15.0; // Example emissions for meat consumption in kg CO2 per kg
                break;
            case "dairy":
                totalEmissions += 8.0; // Example emissions for dairy consumption in kg CO2 per kg
                break;
            case "plant_based":
                totalEmissions += 3.0; // Example emissions for plant-based food consumption in kg CO2 per kg
                break;

        }
    }

    // Method to get the total emissions
    public double getTotalEmissions() {
        return totalEmissions;
    }
}