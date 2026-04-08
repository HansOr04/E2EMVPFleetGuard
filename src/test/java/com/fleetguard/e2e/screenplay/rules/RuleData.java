package com.fleetguard.e2e.screenplay.rules;

import java.util.List;

/**
 * Datos de una regla de mantenimiento.
 *
 * <p>El formulario /rules tiene TRES campos independientes que deben llenarse:</p>
 * <ol>
 *   <li><b>ruleType</b> (buscador autocomplete) → solo acepta valores de {@link MaintenanceRuleType}</li>
 *   <li><b>maintenanceType</b> (select PREVENTIVE/CORRECTIVE) → usar {@link MaintenanceType}</li>
 *   <li><b>vehicleTypes</b> (botones toggle) → usar textos de {@link com.fleetguard.e2e.screenplay.vehicle.VehicleType}</li>
 * </ol>
 * Los campos de km (intervalKm, warningThresholdKm) se derivan de {@link MaintenanceRuleType}
 * y coinciden con los valores del mockMaintenanceRules del backend.
 */
public record RuleData(
        MaintenanceRuleType ruleType,
        MaintenanceType maintenanceType,
        List<String> vehicleTypes
) {
    /** Nombre visible que se escribe en el buscador de /rules. */
    public String name()              { return ruleType.ruleType(); }
    /** Intervalo km según mockMaintenanceRules — valor exacto que auto-completa el form. */
    public int intervalKm()           { return ruleType.intervalKm(); }
    /** Umbral de aviso km según mockMaintenanceRules. */
    public int warningThresholdKm()   { return ruleType.warningThresholdKm(); }
}
