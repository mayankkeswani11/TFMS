package com.project.tfms.dto;

public class FuelEfficiencyDto {

    private Long vehicleId;
    private String registrationNumber;
    private double totalFuelQuantity; // in Liters
    private double totalCost;         // total cost of fuel
    private double averageCostPerLiter; // average cost per liter

    public FuelEfficiencyDto(Long vehicleId, String registrationNumber, double totalFuelQuantity, double totalCost, double averageCostPerLiter) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.totalFuelQuantity = totalFuelQuantity;
        this.totalCost = totalCost;
        this.averageCostPerLiter = averageCostPerLiter;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public double getTotalFuelQuantity() {
        return totalFuelQuantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getAverageCostPerLiter() {
        return averageCostPerLiter;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setTotalFuelQuantity(double totalFuelQuantity) {
        this.totalFuelQuantity = totalFuelQuantity;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setAverageCostPerLiter(double averageCostPerLiter) {
        this.averageCostPerLiter = averageCostPerLiter;
    }
}