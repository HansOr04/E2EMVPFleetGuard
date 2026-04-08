package com.fleetguard.e2e.screenplay.utils;

import com.fleetguard.e2e.screenplay.mileage.MileageData;
import com.fleetguard.e2e.screenplay.rules.RuleData;
import com.fleetguard.e2e.screenplay.vehicle.VehicleData;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Generador de datos de prueba únicos por ejecución.
 * Evita colisiones de placa/VIN duplicados entre ejecuciones paralelas o sucesivas.
 */
public class TestDataGenerator {

    /** Placa única de 7 chars: FG + 5 chars UUID alfanumérico. */
    public static String uniquePlate() {
        return "FG" + UUID.randomUUID().toString()
                .replace("-", "").substring(0, 5).toUpperCase();
    }

    /**
     * VIN de exactamente 17 caracteres.
     * Prefijo estándar NHTSA: "1HGCM" (5) + 12 chars UUID.
     */
    public static String uniqueVin() {
        String suffix = UUID.randomUUID().toString()
                .replace("-", "").substring(0, 12).toUpperCase();
        return "1HGCM" + suffix;
    }

    /** Vehículo Sedán Toyota Corolla 2023 Gasolina con placa y VIN únicos. */
    public static VehicleData uniqueVehicleData() {
        return new VehicleData(
                uniquePlate(), uniqueVin(),
                "Toyota", "Corolla", "2023", "Gasolina", "Sedán"
        );
    }

    /** Vehículo con tipo de vehículo configurable. */
    public static VehicleData uniqueVehicleData(String vehicleType) {
        return new VehicleData(
                uniquePlate(), uniqueVin(),
                "Toyota", "Corolla", "2023", "Gasolina", vehicleType
        );
    }

    /** Datos de actualización de kilometraje. */
    public static MileageData mileageFor(String plate, long mileage) {
        return new MileageData(plate, mileage, "Técnico E2E");
    }

    /** Regla de mantenimiento con nombre único y tipos de vehículo variables. */
    public static RuleData uniqueRuleData(String... vehicleTypes) {
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new RuleData(
                "Regla E2E " + suffix,
                "Preventivo",
                10000,
                500,
                List.of(vehicleTypes)
        );
    }

    /** Fecha de hoy en formato yyyy-MM-dd (requerido por input type=date). */
    public static String today() {
        return LocalDate.now().toString();
    }
}
