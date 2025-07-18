package com.project.tfms.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.tfms.model.Maintenance;


@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}