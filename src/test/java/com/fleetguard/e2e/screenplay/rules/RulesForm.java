package com.fleetguard.e2e.screenplay.rules;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Targets de /rules.
 * Inputs tienen atributo name; botones de tipo de vehículo se ubican por texto.
 * Fuente: SELECTORS.md sección 4.
 */
public class RulesForm {

    public static final Target PAGE_TITLE = Target.the("Título Reglas de Mantenimiento")
            .locatedBy("//h2[normalize-space()='Reglas de Mantenimiento']");

    public static final Target NAME_INPUT = Target.the("Input Nombre de Regla")
            .locatedBy("//input[@name='name']");

    public static final Target MAINTENANCE_TYPE_SELECT = Target.the("Select Tipo de Mantenimiento")
            .locatedBy("//select[@name='maintenanceType']");

    public static final Target INTERVAL_KM_INPUT = Target.the("Input Intervalo km")
            .locatedBy("//input[@name='intervalKm']");

    public static final Target WARNING_THRESHOLD_INPUT = Target.the("Input Umbral de Aviso km")
            .locatedBy("//input[@name='warningThresholdKm']");

    public static final Target SUBMIT_BUTTON = Target.the("Botón Crear Regla")
            .locatedBy("//button[@type='submit' and contains(.,'Crear Regla')]");

    /** Target dinámico para un botón de tipo de vehículo por su texto visible. */
    public static Target vehicleTypeButton(String vehicleType) {
        return Target.the("Botón tipo vehículo '" + vehicleType + "'")
                .locatedBy("//button[normalize-space()='" + vehicleType + "']");
    }
}
