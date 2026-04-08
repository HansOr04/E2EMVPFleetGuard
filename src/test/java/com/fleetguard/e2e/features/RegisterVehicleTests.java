package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.notifications.Toast;
import com.fleetguard.e2e.screenplay.utils.TestDataGenerator;
import com.fleetguard.e2e.screenplay.utils.WaitForToast;
import com.fleetguard.e2e.screenplay.vehicle.RegisterVehicle;
import com.fleetguard.e2e.screenplay.vehicle.VehicleData;
import com.fleetguard.e2e.screenplay.vehicle.VehicleForm;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.annotations.CastMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@ExtendWith(SerenityJUnit5Extension.class)
class RegisterVehicleTests {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldRegisterVehicleSuccessfully() {
        VehicleData data = TestDataGenerator.uniqueVehicleData();

        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(data));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.message(), containsString("registrado")));
    }

    @Test
    void shouldShowValidationErrorsWhenSubmittingEmptyForm() {
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(Click.on(VehicleForm.SUBMIT_BUTTON));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }

    @Test
    void shouldShowErrorWhenVinIsInvalid() {
        // VIN de 16 caracteres en lugar de los 17 requeridos
        VehicleData data = new VehicleData(
                TestDataGenerator.uniquePlate(),
                "INVALIDVIN16CHR1",   // 16 chars — inválido
                "Honda", "Civic", "2022", "Gasolina", "Sedán"
        );

        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(data));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }
}
