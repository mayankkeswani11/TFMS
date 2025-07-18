package com.project.tfms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fuel")
public class Fuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fuelId;

    private long vehicleId;

    private LocalDateTime date;

    private double fuelQuantity;

    private double cost;

    // Getters and Setters

    public int getFuelId() {
        return fuelId;
    }

    public void setFuelId(int fuelId) {
        this.fuelId = fuelId;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getFuelQuantity() {
        return fuelQuantity;
    }

    public void setFuelQuantity(double fuelQuantity) {
        this.fuelQuantity = fuelQuantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}

