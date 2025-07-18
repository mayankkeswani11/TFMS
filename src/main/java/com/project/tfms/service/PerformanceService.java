package com.project.tfms.service;

import com.project.tfms.dto.FuelEfficiencyDto;
import com.project.tfms.dto.MaintenanceCostDto;
import com.project.tfms.dto.TripSummaryDto;
import com.project.tfms.dto.VehiclePerformanceDto; // Import the new DTO
import com.project.tfms.model.Fuel;
import com.project.tfms.model.Performance;
import com.project.tfms.model.Trip;
import com.project.tfms.model.Vehicle;
import com.project.tfms.repository.PerformanceRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private FuelService fuelService;

    @Autowired
    private TripService tripService;

    @Autowired
    private VehicleService vehicleService;


    public List<Performance> getAllPerformanceRecords() {
        return performanceRepository.findAll();
    }

    public Performance savePerformanceRecord(String reportType, String data) {
        Performance performance = new Performance(reportType, data);
        return performanceRepository.save(performance);
    }

    public List<FuelEfficiencyDto> getFuelEfficiencyData() {
        List<Fuel> fuelRecords = fuelService.getAllFuelRecords();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        Map<Long, String> vehicleRegistrationMap = vehicles.stream()
                .collect(Collectors.toMap(Vehicle::getVehicleId, Vehicle::getRegistrationNumber));

        return fuelRecords.stream()
                .collect(Collectors.groupingBy(Fuel::getVehicleId,
                        Collectors.reducing(
                                new double[2], // [totalFuelQuantity, totalCost]
                                fuel -> new double[]{fuel.getFuelQuantity(), fuel.getCost()},
                                (a, b) -> new double[]{a[0] + b[0], a[1] + b[1]}
                        )))
                .entrySet().stream()
                .map(entry -> {
                    Long vehicleId = entry.getKey();
                    double totalFuelQuantity = entry.getValue()[0];
                    double totalCost = entry.getValue()[1];
                    double averageCostPerLiter = (totalFuelQuantity > 0) ? (totalCost / totalFuelQuantity) : 0.0;
                    String registrationNumber = vehicleRegistrationMap.getOrDefault(vehicleId, "N/A");
                    return new FuelEfficiencyDto(vehicleId, registrationNumber, totalFuelQuantity, totalCost, averageCostPerLiter);
                })
                .sorted(Comparator.comparing(FuelEfficiencyDto::getVehicleId))
                .collect(Collectors.toList());
    }


    public List<TripSummaryDto> getTripSummaryData() {
        List<Trip> tripRecords = tripService.getAllTrips();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        Map<Long, String> vehicleRegistrationMap = vehicles.stream()
                .collect(Collectors.toMap(Vehicle::getVehicleId, Vehicle::getRegistrationNumber));

        return tripRecords.stream()
                .filter(trip -> trip.getStartTime() != null && trip.getEndTime() != null)
                .collect(Collectors.groupingBy(Trip::getVehicleId,
                        Collectors.reducing(
                                new long[2], // [tripCount, totalDurationMinutes]
                                trip -> {
                                    Duration duration = Duration.between(trip.getStartTime(), trip.getEndTime());
                                    return new long[]{1, duration.toMinutes()};
                                },
                                (a, b) -> new long[]{a[0] + b[0], a[1] + b[1]}
                        )))
                .entrySet().stream()
                .map(entry -> {
                    Long vehicleId = entry.getKey();
                    long totalTrips = entry.getValue()[0];
                    long totalDurationMinutes = entry.getValue()[1];
                    String registrationNumber = vehicleRegistrationMap.getOrDefault(vehicleId, "N/A");
                    return new TripSummaryDto(vehicleId, registrationNumber, totalTrips, totalDurationMinutes);
                })
                .sorted(Comparator.comparing(TripSummaryDto::getVehicleId))
                .collect(Collectors.toList());
    }


    public List<MaintenanceCostDto> getMaintenanceCostData() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return vehicles.stream()
                .map(vehicle -> new MaintenanceCostDto(vehicle.getVehicleId(), vehicle.getRegistrationNumber(), Math.round(Math.random() * 500000.0) / 100.0)) // Random cost up to 5000.00
                .collect(Collectors.toList());
    }


    public List<VehiclePerformanceDto> getOverallVehiclePerformanceData() {
        List<FuelEfficiencyDto> fuelData = getFuelEfficiencyData();
        List<TripSummaryDto> tripData = getTripSummaryData();
        List<MaintenanceCostDto> maintenanceData = getMaintenanceCostData();

        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        // Create maps for quick lookup by vehicleId
        Map<Long, FuelEfficiencyDto> fuelMap = fuelData.stream()
                .collect(Collectors.toMap(FuelEfficiencyDto::getVehicleId, dto -> dto));
        Map<Long, TripSummaryDto> tripMap = tripData.stream()
                .collect(Collectors.toMap(TripSummaryDto::getVehicleId, dto -> dto));
        Map<Long, MaintenanceCostDto> maintenanceMap = maintenanceData.stream()
                .collect(Collectors.toMap(MaintenanceCostDto::getVehicleId, dto -> dto));

        return vehicles.stream()
                .map(vehicle -> {
                    // Get data for the current vehicle, defaulting to zeros if no records exist
                    FuelEfficiencyDto fuel = fuelMap.getOrDefault(vehicle.getVehicleId(),
                            new FuelEfficiencyDto(vehicle.getVehicleId(), vehicle.getRegistrationNumber(), 0, 0, 0));
                    TripSummaryDto trip = tripMap.getOrDefault(vehicle.getVehicleId(),
                            new TripSummaryDto(vehicle.getVehicleId(), vehicle.getRegistrationNumber(), 0, 0));
                    MaintenanceCostDto maintenance = maintenanceMap.getOrDefault(vehicle.getVehicleId(),
                            new MaintenanceCostDto(vehicle.getVehicleId(), vehicle.getRegistrationNumber(), 0));

                    return new VehiclePerformanceDto(
                            vehicle.getVehicleId(),
                            vehicle.getRegistrationNumber(),
                            fuel.getTotalFuelQuantity(),
                            fuel.getTotalCost(), // Use totalCost here for combined DTO
                            fuel.getAverageCostPerLiter(),
                            trip.getTotalTrips(),
                            trip.getTotalTripDurationMinutes(),
                            maintenance.getTotalMaintenanceCost()
                    );
                })
                .sorted(Comparator.comparing(VehiclePerformanceDto::getVehicleId))
                .collect(Collectors.toList());
    }




    public ByteArrayOutputStream generateOverallPerformanceExcelReport() throws IOException {
        List<VehiclePerformanceDto> performanceData = getOverallVehiclePerformanceData();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Fleet Performance Report");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Vehicle ID", "Registration Number", "Total Fuel (L)", "Total Fuel Cost (₹)",
                "Avg Cost/Liter (₹)", "Total Trips", "Total Trip Duration (Mins)", "Total Maintenance Cost (₹)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (VehiclePerformanceDto data : performanceData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getVehicleId());
            row.createCell(1).setCellValue(data.getRegistrationNumber());
            row.createCell(2).setCellValue(data.getTotalFuelQuantity());
            row.createCell(3).setCellValue(data.getTotalFuelCost());
            row.createCell(4).setCellValue(data.getAverageCostPerLiter());
            row.createCell(5).setCellValue(data.getTotalTrips());
            row.createCell(6).setCellValue(data.getTotalTripDurationMinutes());
            row.createCell(7).setCellValue(data.getTotalMaintenanceCost());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }
}