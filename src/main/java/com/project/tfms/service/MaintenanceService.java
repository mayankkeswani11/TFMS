package com.project.tfms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.tfms.model.Maintenance;
import com.project.tfms.repository.MaintenanceRepository;
import com.project.tfms.repository.VehicleRepository;




@Service
public class MaintenanceService{

	   @Autowired
       private VehicleRepository vehicleRepository;

	    @Autowired
	    private MaintenanceRepository maintenanceRepository;


	    public List<Maintenance> getAllMaintenances() {
	        return maintenanceRepository.findAll();
	    }


	    public Optional<Maintenance> getMaintenanceById(Long id) {
	        return maintenanceRepository.findById(id);
	    }

	    public boolean isRegisteredVehicleId(Long vehicleId) {
	        return vehicleRepository.existsById(vehicleId);
	    }

	    public Maintenance scheduleMaintenance(Maintenance maintenance) {
	        return maintenanceRepository.save(maintenance);
	    }


	    public Maintenance updateMaintenance(Maintenance maintenance) {
	        return maintenanceRepository.save(maintenance);
	    }


	    public void deleteMaintenance(Long id) {
	        maintenanceRepository.deleteById(id);
	    }




	}