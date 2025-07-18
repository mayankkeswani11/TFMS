package com.project.tfms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.tfms.model.Fuel;
import com.project.tfms.repository.FuelRepository;

import java.util.List;

@Service
public class FuelService {

    @Autowired
    private FuelRepository fuelRepository;

    public Fuel addFuelRecord(Fuel fuel) {
        return fuelRepository.save(fuel);
    }

    public List<Fuel> getAllFuelRecords() {
        return fuelRepository.findAll();
    }
}
