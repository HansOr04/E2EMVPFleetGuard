package com.fleetguard.e2e.screenplay.rules;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

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
        actor.attemptsTo(
                Enter.theValue(data.name()).into(RulesForm.NAME_INPUT),
                SelectFromOptions.byVisibleText(data.maintenanceType())
                        .from(RulesForm.MAINTENANCE_TYPE_SELECT),
                Enter.theValue(String.valueOf(data.intervalKm()))
                        .into(RulesForm.INTERVAL_KM_INPUT),
                Enter.theValue(String.valueOf(data.warningThresholdKm()))
                        .into(RulesForm.WARNING_THRESHOLD_INPUT)
        );
        for (String vehicleType : data.vehicleTypes()) {
            actor.attemptsTo(Click.on(RulesForm.vehicleTypeButton(vehicleType)));
        }
        actor.attemptsTo(Click.on(RulesForm.SUBMIT_BUTTON));
    }
}
