package com.fleetguard.e2e.screenplay.services;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

public class QueryAlerts implements Task {

    private final String plate;

    public QueryAlerts(String plate) {
        this.plate = plate;
    }

    public static QueryAlerts forPlate(String plate) {
        return Tasks.instrumented(QueryAlerts.class, plate);
    }

    @Step("{0} queries maintenance alerts for plate '#plate'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(plate).into(ServicesForm.PLATE_INPUT),
                Click.on(ServicesForm.QUERY_ALERTS_BUTTON)
        );
    }
}
