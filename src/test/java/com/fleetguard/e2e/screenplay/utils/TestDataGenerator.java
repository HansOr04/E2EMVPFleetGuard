package com.fleetguard.e2e.screenplay.utils;

import com.fleetguard.e2e.screenplay.mileage.MileageData;
import com.fleetguard.e2e.screenplay.rules.MaintenanceRuleType;
import com.fleetguard.e2e.screenplay.rules.RuleData;
import com.fleetguard.e2e.screenplay.vehicle.VehicleData;
import com.fleetguard.e2e.screenplay.vehicle.VehicleType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Generador de datos de prueba únicos por ejecución.
 *
 * <p>Restricciones de datos:</p>
 * <ul>
 *   <li><b>Tipos de vehículo:</b> usar {@link VehicleType} — 15 registros fijos en vehicle_type.</li>
 *   <li><b>Tipos de regla:</b> usar {@link MaintenanceRuleType} — solo los 51 tipos de
 *       mockMaintenanceRules son válidos. Valores libres → "No se encontraron reglas".</li>
 * </ul>
 */
public class TestDataGenerator {

    /** Placa única de 7 chars: FG + 5 chars UUID alfanumérico. */
    public static String uniquePlate() {
        return "FG" + UUID.randomUUID().toString()
                .replace("-", "").substring(0, 5).toUpperCase();
    }

    /**
     * VIN de exactamente 17 caracteres.
     * Prefijo NHTSA estándar: "1HGCM" (5) + 12 chars UUID.
     */
    public static String uniqueVin() {
        String suffix = UUID.randomUUID().toString()
                .replace("-", "").substring(0, 12).toUpperCase();
        return "1HGCM" + suffix;
    }

    /** Vehículo Sedán Toyota Corolla 2023 Gasolina — tipo por defecto. */
    public static VehicleData uniqueVehicleData() {
        return uniqueVehicleData(VehicleType.SEDAN);
    }

    /** Vehículo con tipo configurable usando {@link VehicleType}. */
    public static VehicleData uniqueVehicleData(VehicleType vehicleType) {
        return new VehicleData(
                uniquePlate(), uniqueVin(),
                "Toyota", "Corolla", "2023", "Gasolina", vehicleType.text()
        );
    }

    /** Datos de actualización de kilometraje. */
    public static MileageData mileageFor(String plate, long mileage) {
        return new MileageData(plate, mileage, "Técnico E2E");
    }

    /**
     * Regla de mantenimiento con tipo válido de {@link MaintenanceRuleType}.
     * Los km se derivan del enum — coinciden con los valores del mock del backend.
     */
    public static RuleData ruleFor(MaintenanceRuleType ruleType, VehicleType... vehicleTypes) {
        List<String> types = Arrays.stream(vehicleTypes)
                .map(VehicleType::text)
                .collect(Collectors.toList());
        return new RuleData(ruleType, types);
    }

    /** Regla de aceite de motor liviano para Sedán — combinación más común en tests. */
    public static RuleData defaultRuleData() {
        return ruleFor(MaintenanceRuleType.ACEITE_MOTOR_LIVIANO, VehicleType.SEDAN);
    }

    /** Fecha de hoy en formato yyyy-MM-dd (requerido por input type=date). */
    public static String today() {
        return LocalDate.now().toString();
    }
}
