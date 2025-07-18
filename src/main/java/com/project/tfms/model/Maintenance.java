package com.project.tfms.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceId;

    private Long vehicleId;

    private String description;

    private Double cost;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduledDate;

    private String status; // e.g., "Scheduled", "Completed", "Cancelled"

    public Long getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() { // Getter for cost
        return cost;
    }

    public void setCost(Double cost) { // Setter for cost
        this.cost = cost;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}