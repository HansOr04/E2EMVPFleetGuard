package com.fleetguard.e2e.screenplay.mileage;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Hit;
import org.openqa.selenium.Keys;

public class UpdateMileage implements Task {

    private final MileageData data;

    public UpdateMileage(MileageData data) {
        this.data = data;
    }

    public static UpdateMileage with(MileageData data) {
        return Tasks.instrumented(UpdateMileage.class, data);
    }

    @Step("{0} updates mileage for plate '#data.plate' to #data.mileage km")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(data.plate()).into(MileageForm.PLATE_INPUT),
                // Ingresar km y hacer TAB para disparar blur y evitar interferencia de onWheel
                Enter.theValue(String.valueOf(data.mileage())).into(MileageForm.MILEAGE_INPUT),
                Hit.the(Keys.TAB).into(MileageForm.MILEAGE_INPUT),
                Enter.theValue(data.recordedBy()).into(MileageForm.RECORDED_BY_INPUT),
                Click.on(MileageForm.SUBMIT_BUTTON)
        );
    }
}
