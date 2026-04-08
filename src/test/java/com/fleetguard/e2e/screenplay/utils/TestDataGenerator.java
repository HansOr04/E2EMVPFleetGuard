package com.fleetguard.e2e.screenplay.utils;

import com.fleetguard.e2e.screenplay.mileage.MileageData;
import com.fleetguard.e2e.screenplay.rules.MaintenanceRuleType;
import com.fleetguard.e2e.screenplay.rules.MaintenanceType;
import com.fleetguard.e2e.screenplay.rules.RuleData;
import com.fleetguard.e2e.screenplay.vehicle.VehicleData;
import com.fleetguard.e2e.screenplay.vehicle.VehicleType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Generador de datos de prueba para FleetGuard E2E.
 *
 * <p><b>Restricciones de datos — usar siempre los enums:</b></p>
 * <ul>
 *   <li>{@link VehicleType} — 15 tipos fijos de la tabla vehicle_type.</li>
 *   <li>{@link MaintenanceRuleType} — 51 tipos de mockMaintenanceRules (texto libre → falla).</li>
 *   <li>{@link MaintenanceType} — PREVENTIVO / CORRECTIVO (select required en /rules).</li>
 * </ul>
 */
public class TestDataGenerator {

    private TestDataGenerator() { /* utility class */ }

    /** Placa única: FG + 5 chars UUID. */
    public static String uniquePlate() {
        return "FG" + UUID.randomUUID().toString()
                .replace("-", "").substring(0, 5).toUpperCase();
    }

    /** VIN de exactamente 17 caracteres. */
    public static String uniqueVin() {
        String suffix = UUID.randomUUID().toString()
                .replace("-", "").substring(0, 12).toUpperCase();
        return "1HGCM" + suffix;
    }

    /** Vehículo Sedán Toyota Corolla 2023 Gasolina. */
    public static VehicleData uniqueVehicleData() {
        return uniqueVehicleData(VehicleType.SEDAN);
    }

    /** Vehículo con tipo de vehículo configurable. */
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
     * Regla de mantenimiento Preventivo para los tipos de vehículo indicados.
     * El tipo de mantenimiento por defecto es {@link MaintenanceType#PREVENTIVO}.
     */
    public static RuleData ruleFor(MaintenanceRuleType ruleType, VehicleType... vehicleTypes) {
        return ruleFor(ruleType, MaintenanceType.PREVENTIVO, vehicleTypes);
    }

    /**
     * Regla de mantenimiento con tipo de mantenimiento explícito.
     */
    public static RuleData ruleFor(MaintenanceRuleType ruleType,
                                   MaintenanceType maintenanceType,
                                   VehicleType... vehicleTypes) {
        List<String> types = Arrays.stream(vehicleTypes)
                .map(VehicleType::text)
                .collect(Collectors.toList());
        return new RuleData(ruleType, maintenanceType, types);
    }

    /** Regla de aceite motor liviano Preventivo para Sedán — combinación por defecto. */
    public static RuleData defaultRuleData() {
        return ruleFor(MaintenanceRuleType.ACEITE_MOTOR_LIVIANO, VehicleType.SEDAN);
    }

    /** Fecha de hoy en formato yyyy-MM-dd (input type=date). */
    public static String today() {
        return LocalDate.now().toString();
    }
}
