package com.fleetguard.e2e.screenplay.rules;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

/**
 * Task: Crear una regla de mantenimiento en /rules.
 *
 * <p><b>Orden de interacción en el formulario:</b>
 * <ol>
 *   <li>Escribir nombre de regla en el buscador (filtra mockMaintenanceRules)</li>
 *   <li>Esperar sugerencia del autocomplete y hacer click en ella</li>
 *   <li>Seleccionar Tipo de Mantenimiento en el select (REQUIRED — no tiene valor por defecto)</li>
 *   <li>Asegurar que km se completaron (auto-fill del autocomplete o entrada manual)</li>
 *   <li>Click en botones de tipo de vehículo (mínimo 1)</li>
 *   <li>Click en "Crear Regla"</li>
 * </ol>
 * </p>
 *
 * <p><b>REGLA CRÍTICA:</b> {@code ruleType} DEBE ser {@link MaintenanceRuleType}.
 * Texto libre → "No se encontraron reglas" → sin toast → test falla.</p>
 */
public class CreateMaintenanceRule implements Task {

    private final RuleData data;

    public CreateMaintenanceRule(RuleData data) {
        this.data = data;
    }

    public static CreateMaintenanceRule with(RuleData data) {
        return Tasks.instrumented(CreateMaintenanceRule.class, data);
    }

    @Step("{0} creates maintenance rule '#data.name'")
    @Override
    public <T extends Actor> void performAs(T actor) {

        // ── 1. Escribir en el buscador para activar el autocomplete ────────
        actor.attemptsTo(
                Enter.theValue(data.name()).into(RulesForm.NAME_INPUT)
        );

        // ── 2. Esperar la sugerencia y hacer click para seleccionarla ──────
        // Al seleccionar, el frontend puede auto-completar los campos de km
        actor.attemptsTo(
                WaitUntil.the(RulesForm.firstSuggestion(data.name()), isClickable())
                        .forNoMoreThan(5).seconds(),
                Click.on(RulesForm.firstSuggestion(data.name()))
        );

        // ── 3. Seleccionar Tipo de Mantenimiento (select REQUIRED) ─────────
        // Este campo es INDEPENDIENTE del autocomplete — NUNCA se auto-completa.
        // El select tiene value="" por defecto (disabled) → debe seleccionarse explícitamente.
        actor.attemptsTo(
                SelectFromOptions.byVisibleText(data.maintenanceType().text())
                        .from(RulesForm.MAINTENANCE_TYPE_SELECT)
        );

        // ── 4. Asegurar que los campos km tienen valor (por si no auto-completaron) ──
        // Clear primero para no duplicar el valor si ya fue llenado por el autocomplete
        actor.attemptsTo(
                Clear.field(RulesForm.INTERVAL_KM_INPUT),
                Enter.theValue(String.valueOf(data.intervalKm()))
                        .into(RulesForm.INTERVAL_KM_INPUT),
                Clear.field(RulesForm.WARNING_THRESHOLD_INPUT),
                Enter.theValue(String.valueOf(data.warningThresholdKm()))
                        .into(RulesForm.WARNING_THRESHOLD_INPUT)
        );

        // ── 5. Seleccionar tipos de vehículo (mínimo 1 requerido) ──────────
        for (String vehicleType : data.vehicleTypes()) {
            actor.attemptsTo(Click.on(RulesForm.vehicleTypeButton(vehicleType)));
        }

        // ── 6. Enviar el formulario ────────────────────────────────────────
        actor.attemptsTo(Click.on(RulesForm.SUBMIT_BUTTON));
    }
}
