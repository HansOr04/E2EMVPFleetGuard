package com.fleetguard.e2e.screenplay.services;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

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
        actor.attemptsTo(
                // Esperar que la primera alerta esté visible antes de interactuar
                WaitUntil.the(ServicesForm.FIRST_ALERT, isVisible()).forNoMoreThan(10).seconds(),
                Click.on(ServicesForm.FIRST_ALERT),
                Enter.theValue(data.recordedBy()).into(ServicesForm.RECORDED_BY_INPUT),
                Enter.theValue(data.performedAt()).into(ServicesForm.PERFORMED_AT_INPUT),
                Enter.theValue(data.mileageAtService()).into(ServicesForm.MILEAGE_AT_SERVICE_INPUT),
                Click.on(ServicesForm.SUBMIT_BUTTON)
        );
    }
}
