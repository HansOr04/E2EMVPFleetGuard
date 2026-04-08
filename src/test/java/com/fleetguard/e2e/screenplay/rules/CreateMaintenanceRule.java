package com.fleetguard.e2e.screenplay.rules;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

/**
 * Task: Crear una regla de mantenimiento en /rules.
 *
 * <p><b>Flujo del formulario:</b>
 * <ol>
 *   <li>Escribir el nombre de la regla en el buscador — filtra mockMaintenanceRules</li>
 *   <li>Esperar a que aparezca la primera sugerencia en el autocomplete</li>
 *   <li>Hacer click en la sugerencia — los campos km se auto-completan</li>
 *   <li>Click en uno o más botones de tipo de vehículo</li>
 *   <li>Click en "Crear Regla"</li>
 * </ol>
 * </p>
 *
 * <p>El {@code ruleType} DEBE ser un valor de {@link MaintenanceRuleType}.
 * Usar un texto libre que no exista en el mock produce "No se encontraron reglas"
 * y el backend rechaza la creación sin mostrar toast.</p>
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
        // Paso 1: Escribir el nombre en el buscador para que aparezcan las sugerencias
        actor.attemptsTo(
                Enter.theValue(data.name()).into(RulesForm.NAME_INPUT)
        );

        // Paso 2: Esperar a que la primera sugerencia del autocomplete sea clickeable
        // y seleccionarla para que los campos km se auto-completen
        actor.attemptsTo(
                WaitUntil.the(RulesForm.firstSuggestion(data.name()), isClickable())
                        .forNoMoreThan(5).seconds(),
                Click.on(RulesForm.firstSuggestion(data.name()))
        );

        // Paso 3: Seleccionar tipos de vehículo (mínimo 1 requerido)
        for (String vehicleType : data.vehicleTypes()) {
            actor.attemptsTo(Click.on(RulesForm.vehicleTypeButton(vehicleType)));
        }

        // Paso 4: Enviar el formulario
        actor.attemptsTo(Click.on(RulesForm.SUBMIT_BUTTON));
    }
}
