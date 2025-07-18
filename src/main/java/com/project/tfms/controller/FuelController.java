package com.project.tfms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.tfms.model.Fuel;
import com.project.tfms.model.Vehicle;
import com.project.tfms.service.FuelService;
import com.project.tfms.service.VehicleService;

import java.util.List;

@Controller
public class FuelController {

    @Autowired
    private FuelService fuelService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/fuel")
    public String showFuelForm(Model model) {
        model.addAttribute("fuel", new Fuel());
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles);
        return "index";
    }

    @PostMapping("/fuel/add")
    public String addFuelRecord(@ModelAttribute("fuel") Fuel fuel, Model model) {
        // Check if the vehicle exists
        if (!vehicleService.isRegisteredVehicleId(fuel.getVehicleId())) {
            model.addAttribute("error", "Vehicle ID not found. Please select a registered vehicle."); // Changed error message
            model.addAttribute("fuel", fuel); // retain entered data
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "index";
        }

        fuelService.addFuelRecord(fuel);
        return "redirect:/usage";
    }

    // Show fuel usage table
    @GetMapping("/usage")
    public String viewFuelUsage(Model model) {
        List<Fuel> fuelRecords = fuelService.getAllFuelRecords();
        model.addAttribute("fuelRecords", fuelRecords);
        return "usage";
    }
}