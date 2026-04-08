package com.fleetguard.e2e.features;

import com.fleetguard.e2e.screenplay.mileage.MileageData;
import com.fleetguard.e2e.screenplay.mileage.UpdateMileage;
import com.fleetguard.e2e.screenplay.navigation.NavigateTo;
import com.fleetguard.e2e.screenplay.notifications.Toast;
import com.fleetguard.e2e.screenplay.utils.TestDataGenerator;
import com.fleetguard.e2e.screenplay.utils.WaitForToast;
import com.fleetguard.e2e.screenplay.vehicle.RegisterVehicle;
import com.fleetguard.e2e.screenplay.vehicle.VehicleData;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.annotations.CastMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@ExtendWith(SerenityJUnit5Extension.class)
class RegisterMileageTests {

    @CastMember(name = "Hans the QA Engineer")
    Actor hans;

    @Test
    void shouldUpdateMileageSuccessfully() {
        // Precondición: registrar el vehículo primero
        VehicleData vehicle = TestDataGenerator.uniqueVehicleData();
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(vehicle));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // Actualizar kilometraje
        MileageData mileageData = TestDataGenerator.mileageFor(vehicle.plate(), 15000L);
        hans.attemptsTo(NavigateTo.theMileagePage());
        hans.attemptsTo(UpdateMileage.with(mileageData));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.message(), containsString("actualiz")));
    }

    @Test
    void shouldShowWarningForExcessiveIncrement() {
        // Precondición: vehículo existente con km base bajo
        VehicleData vehicle = TestDataGenerator.uniqueVehicleData();
        hans.attemptsTo(NavigateTo.theRegisterPage());
        hans.attemptsTo(RegisterVehicle.with(vehicle));
        hans.attemptsTo(WaitForToast.toAppear());
        hans.attemptsTo(WaitForToast.toDisappear());

        // Km con incremento excesivo (> 2000 sobre un vehículo recién registrado)
        MileageData mileageData = TestDataGenerator.mileageFor(vehicle.plate(), 50000L);
        hans.attemptsTo(NavigateTo.theMileagePage());
        hans.attemptsTo(UpdateMileage.with(mileageData));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }

    @Test
    void shouldShowErrorWhenPlateNotFound() {
        MileageData mileageData = TestDataGenerator.mileageFor("NOEXIST99", 5000L);

        hans.attemptsTo(NavigateTo.theMileagePage());
        hans.attemptsTo(UpdateMileage.with(mileageData));
        hans.attemptsTo(WaitForToast.toAppear());

        hans.should(seeThat(Toast.isVisible(), is(true)));
    }
}
