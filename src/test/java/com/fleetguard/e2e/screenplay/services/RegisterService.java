package com.fleetguard.e2e.screenplay.services;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Task: Registrar un servicio de mantenimiento en /services (Paso 2).
 *
 * <p><b>Prerequisito:</b> La placa ya fue consultada (Paso 1 — {@link QueryAlerts}) y
 * la lista de alertas es visible.</p>
 *
 * <p><b>Orden obligatorio de campos:</b>
 * <ol>
 *   <li>Click en la alerta a resolver (button[@type='button'])</li>
 *   <li>recordedBy (required) — habilita parcialmente el submit</li>
 *   <li>performedAt (required) — ya tiene fecha por defecto pero debe confirmarse</li>
 *   <li>provider (opcional)</li>
 *   <li>cost (opcional)</li>
 *   <li>description (opcional)</li>
 *   <li>mileageAtService (required) — el submit se habilita cuando todos los required están llenos</li>
 *   <li>Esperar submit habilitado → Click</li>
 * </ol>
 * </p>
 */
public class RegisterService implements Task {

    private final ServiceData data;

    public RegisterService(ServiceData data) {
        this.data = data;
    }

    public static RegisterService with(ServiceData data) {
        return Tasks.instrumented(RegisterService.class, data);
    }

    @Step("{0} registers maintenance service for plate '#data.plate'")
    @Override
    public <T extends Actor> void performAs(T actor) {

        // ── 1. Esperar y seleccionar la primera alerta (button, no div) ────
        actor.attemptsTo(
                WaitUntil.the(ServicesForm.FIRST_ALERT, isVisible()).forNoMoreThan(10).seconds(),
                Click.on(ServicesForm.FIRST_ALERT)
        );

        // ── 2. Llenar campos required primero (habilitan el submit) ────────
        actor.attemptsTo(
                Enter.theValue(data.recordedBy()).into(ServicesForm.RECORDED_BY_INPUT),
                Enter.theValue(data.performedAt()).into(ServicesForm.PERFORMED_AT_INPUT)
        );

        // ── 3. Campos opcionales ───────────────────────────────────────────
        if (data.provider() != null && !data.provider().isEmpty()) {
            actor.attemptsTo(Enter.theValue(data.provider()).into(ServicesForm.PROVIDER_INPUT));
        }
        if (data.cost() != null && !data.cost().isEmpty()) {
            actor.attemptsTo(Enter.theValue(data.cost()).into(ServicesForm.COST_INPUT));
        }
        if (data.description() != null && !data.description().isEmpty()) {
            actor.attemptsTo(Enter.theValue(data.description()).into(ServicesForm.DESCRIPTION_TEXTAREA));
        }

        // ── 4. Kilometraje (required) — al llenarlo el submit se habilita ─
        actor.attemptsTo(
                Enter.theValue(data.mileageAtService()).into(ServicesForm.MILEAGE_AT_SERVICE_INPUT)
        );

        // ── 5. Esperar que el submit se habilite y hacer click ─────────────
        // El botón tiene disabled="" hasta que todos los required están llenos
        actor.attemptsTo(
                WaitUntil.the(ServicesForm.SUBMIT_BUTTON, isClickable()).forNoMoreThan(5).seconds(),
                Click.on(ServicesForm.SUBMIT_BUTTON)
        );
    }
}
