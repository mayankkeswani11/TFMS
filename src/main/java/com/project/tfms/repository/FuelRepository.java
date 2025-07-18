package com.project.tfms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.tfms.model.Fuel;



@Repository
public interface FuelRepository extends JpaRepository<Fuel, Integer> {
}
