package com.project.tfms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.tfms.model.Maintenance;
import com.project.tfms.model.Vehicle; // <--- ADD THIS IMPORT
import com.project.tfms.repository.MaintenanceRepository;
import com.project.tfms.service.VehicleService; // Assuming VehicleService has getAllVehicles() method

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/maintenances")
public class MaintenanceController {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private VehicleService vehicleService; // Used to fetch vehicle data

    @GetMapping
    public String listMaintenances(Model model) {
        model.addAttribute("maintenances", maintenanceRepository.findAll());
        return "maintenance-list";
    }

    @GetMapping("/new")
    public String showScheduleForm(Model model) {
        model.addAttribute("maintenance", new Maintenance());
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles); // Add the list of vehicles to the model
        return "maintenance-form";
    }

    @PostMapping("/schedule")
    public String scheduleMaintenance(@ModelAttribute Maintenance maintenance, Model model) {
        if (!vehicleService.isRegisteredVehicleId(maintenance.getVehicleId())) {
            model.addAttribute("maintenance", maintenance);
            model.addAttribute("error", "Vehicle ID is not registered. Please select a valid vehicle."); // Improved message
            // --- CHANGE START: Re-add vehicles on error ---
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            // --- CHANGE END ---
            return "maintenance-form";
        }

        maintenanceRepository.save(maintenance);
        return "redirect:/maintenances";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Maintenance> maintenance = maintenanceRepository.findById(id);
        if (maintenance.isPresent()) {
            model.addAttribute("maintenance", maintenance.get());
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "maintenance-form";
        }
        return "redirect:/maintenances";
    }

    @PostMapping("/update")
    public String updateMaintenance(@ModelAttribute Maintenance maintenance, Model model) {
        if (!vehicleService.isRegisteredVehicleId(maintenance.getVehicleId())) {
            model.addAttribute("maintenance", maintenance);
            model.addAttribute("error", "Vehicle ID is not registered. Please select a valid vehicle."); // Improved message
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "maintenance-form";
        }

        maintenanceRepository.save(maintenance);
        return "redirect:/maintenances";
    }

    @GetMapping("/delete/{id}")
    public String deleteMaintenance(@PathVariable Long id) {
        maintenanceRepository.deleteById(id);
        return "redirect:/maintenances";
    }
}