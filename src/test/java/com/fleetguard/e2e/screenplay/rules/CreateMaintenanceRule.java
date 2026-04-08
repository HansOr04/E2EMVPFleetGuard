package com.fleetguard.e2e.screenplay.rules;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;
import org.openqa.selenium.Keys;

/**
 * Task: Crear una regla de mantenimiento en /rules.
 *
 * <p>Flujo del formulario:</p>
 * <ol>
 *   <li>Escribir nombre en el buscador (activa el autocomplete)</li>
 *   <li>Seleccionar la primera sugerencia con ARROW_DOWN + RETURN (teclado robustamente)</li>
 *   <li>Seleccionar Tipo de Mantenimiento (select REQUIRED — independiente del autocomplete)</li>
 *   <li>Confirmar/rellenar campos km (defensivo en caso de que el autocomplete no auto-completó)</li>
 *   <li>Click en botones de tipo de vehículo</li>
 *   <li>Click en "Crear Regla"</li>
 * </ol>
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

        // ── 1. Escribir nombre en el buscador ──────────────────────────────
        actor.attemptsTo(
                Enter.theValue(data.name()).into(RulesForm.NAME_INPUT)
        );

        // ── 2. Navegar el autocomplete con teclado (ARROW_DOWN + RETURN) ──
        // Este enfoque es robusto: no depende del selector del DOM del dropdown.
        // ARROW_DOWN navega a la primera sugerencia; RETURN la selecciona.
        actor.attemptsTo(Task.where("{0} selects first suggestion via keyboard",
                a -> {
                    try { Thread.sleep(800); } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    RulesForm.NAME_INPUT.resolveFor(a).sendKeys(Keys.ARROW_DOWN);
                    try { Thread.sleep(300); } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    RulesForm.NAME_INPUT.resolveFor(a).sendKeys(Keys.RETURN);
                }
        ));

        // ── 3. Seleccionar Tipo de Mantenimiento (select REQUIRED) ────────
        // El dropdown de tipo NO se auto-completa desde el autocomplete.
        actor.attemptsTo(
                SelectFromOptions.byVisibleText(data.maintenanceType().text())
                        .from(RulesForm.MAINTENANCE_TYPE_SELECT)
        );

        // ── 4. Confirmar campos km (defensivo: rellenar si no se auto-completaron) ──
        actor.attemptsTo(
                Clear.field(RulesForm.INTERVAL_KM_INPUT),
                Enter.theValue(String.valueOf(data.intervalKm()))
                        .into(RulesForm.INTERVAL_KM_INPUT),
                Clear.field(RulesForm.WARNING_THRESHOLD_INPUT),
                Enter.theValue(String.valueOf(data.warningThresholdKm()))
                        .into(RulesForm.WARNING_THRESHOLD_INPUT)
        );

        // ── 5. Seleccionar tipos de vehículo (mínimo 1) ───────────────────
        for (String vehicleType : data.vehicleTypes()) {
            actor.attemptsTo(Click.on(RulesForm.vehicleTypeButton(vehicleType)));
        }

        // ── 6. Enviar el formulario ────────────────────────────────────────
        actor.attemptsTo(Click.on(RulesForm.SUBMIT_BUTTON));
    }
}
