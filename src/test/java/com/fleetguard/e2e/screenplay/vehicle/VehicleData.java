package com.fleetguard.e2e.screenplay.vehicle;

public record VehicleData(
        String plate,
        String vin,
        String brand,
        String model,
        String year,
        String fuelType,
        String vehicleType
) {}
