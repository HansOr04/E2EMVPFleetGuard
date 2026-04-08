package com.fleetguard.e2e.screenplay.rules;

import net.serenitybdd.screenplay.targets.Target;

/**
 * Targets de /rules — Reglas de Mantenimiento.
 * Fuente: SELECTORS.md sección 4.
 *
 * <p><b>Comportamiento del campo "Nombre de la Regla":</b>
 * Es un BUSCADOR con autocomplete que filtra los 51 tipos de {@link MaintenanceRuleType}.
 * Al escribir, aparece una lista desplegable con las coincidencias.
 * Se debe seleccionar la primera sugerencia para que los campos de km se auto-completen.
 * Si el texto no coincide con ninguno de los 51 tipos → "No se encontraron reglas"
 * y el backend rechaza la creación.</p>
 */
public class RulesForm {

    public static final Target PAGE_TITLE = Target.the("Título Reglas de Mantenimiento")
            .locatedBy("//h2[normalize-space()='Reglas de Mantenimiento']");

    /** Buscador de regla — solo acepta valores de mockMaintenanceRules. */
    public static final Target NAME_INPUT = Target.the("Input Nombre de Regla (buscador)")
            .locatedBy("//input[@name='name']");

    /**
     * Primera sugerencia del autocomplete — aparece mientras se escribe en NAME_INPUT.
     * La lista puede ser un &lt;ul&gt;/&lt;li&gt; o &lt;div&gt;s con el texto de la regla.
     * Hacemos click en el primer elemento que contenga el texto buscado.
     */
    public static Target firstSuggestion(String ruleTypeText) {
        return Target.the("Sugerencia autocomplete '" + ruleTypeText + "'")
                .locatedBy("(//*[contains(normalize-space(),'" + ruleTypeText + "') " +
                        "and not(self::input) and not(self::h2) and not(self::h3) " +
                        "and not(self::label)])[1]");
    }

    /** Mensaje inline cuando el texto buscado no coincide con ningún tipo de regla. */
    public static final Target NO_MATCH_MESSAGE = Target.the("Mensaje 'No se encontraron reglas'")
            .locatedBy("//*[contains(normalize-space(),'No se encontraron reglas')]");

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
