package com.fleetguard.e2e.screenplay.rules;

import java.util.List;

/**
 * Datos de una regla de mantenimiento.
 *
 * <p>{@code ruleType} DEBE ser un valor de {@link MaintenanceRuleType} —
 * los intervalos km se derivan de él para garantizar consistencia con el mock del backend.</p>
 */
public record RuleData(
        MaintenanceRuleType ruleType,
        List<String> vehicleTypes
) {
    /** Nombre visible que se escribe en el buscador de /rules. */
    public String name()              { return ruleType.ruleType(); }
    /** Intervalo km según mockMaintenanceRules — se verifica pero normalmente auto-completa. */
    public int intervalKm()           { return ruleType.intervalKm(); }
    /** Umbral de aviso km según mockMaintenanceRules. */
    public int warningThresholdKm()   { return ruleType.warningThresholdKm(); }
}
