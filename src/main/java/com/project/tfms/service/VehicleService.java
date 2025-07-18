package com.project.tfms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.tfms.model.Vehicle;
import com.project.tfms.repository.VehicleRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    // Fetch all vehicles
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // Check if vehicle ID exists
    public boolean isRegisteredVehicleId(Long vehicleId) {
        return vehicleRepository.existsById(vehicleId);
    }

    // Return all registered vehicle IDs for dropdown
    public List<Long> getAllVehicleIds() {
        return vehicleRepository.findAll()
                .stream()
                .map(vehicle -> vehicle.getVehicleId()) // Adjust getter if needed
                .toList();
    }

    // Check if a vehicle exists by registration number
    public boolean existsByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(registrationNumber).isPresent();
    }

    // Add a vehicle
    public void addVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    // Get vehicle by ID
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    // Update a vehicle
    public void updateVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    // Delete a vehicle
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
