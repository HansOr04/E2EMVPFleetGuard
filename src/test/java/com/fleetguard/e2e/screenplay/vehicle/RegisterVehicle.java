package com.fleetguard.e2e.screenplay.vehicle;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

public class RegisterVehicle implements Task {

    private final VehicleData data;

    public RegisterVehicle(VehicleData data) {
        this.data = data;
    }

    public static RegisterVehicle with(VehicleData data) {
        return Tasks.instrumented(RegisterVehicle.class, data);
    }

    @Step("{0} registers vehicle with plate '#data.plate'")
    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(data.plate()).into(VehicleForm.PLATE_INPUT),
                Enter.theValue(data.vin()).into(VehicleForm.VIN_INPUT),
                Enter.theValue(data.brand()).into(VehicleForm.BRAND_INPUT),
                Enter.theValue(data.model()).into(VehicleForm.MODEL_INPUT),
                SelectFromOptions.byVisibleText(data.year()).from(VehicleForm.YEAR_SELECT),
                SelectFromOptions.byVisibleText(data.fuelType()).from(VehicleForm.FUEL_TYPE_SELECT),
                SelectFromOptions.byVisibleText(data.vehicleType()).from(VehicleForm.VEHICLE_TYPE_SELECT),
                Click.on(VehicleForm.SUBMIT_BUTTON)
        );
    }
}
